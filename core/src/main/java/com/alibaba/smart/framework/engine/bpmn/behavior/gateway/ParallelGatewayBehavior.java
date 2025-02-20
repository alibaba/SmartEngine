package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.alibaba.smart.framework.engine.behavior.ActivityBehavior;
import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.common.util.InstanceUtil;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.ParallelServiceOrchestration;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.PvmActivityTask;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.ParallelGatewayConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.ContextFactory;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.alibaba.smart.framework.engine.util.ParallelServiceOrchestrationUtil.*;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = ParallelGateway.class)
public class ParallelGatewayBehavior extends AbstractActivityBehavior<ParallelGateway> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelGatewayBehavior.class);


    public ParallelGatewayBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {

        //算法说明:ParallelGatewayBehavior 同时承担 fork 和 join 职责。所以说,如何判断是 fork 还是 join ?
        // 目前主要就看pvmActivity节点的 incomeTransition 和 outcomeTransition 数量差异。
        // 如果 income 为1,则为 join 节点。
        // 如果 outcome 为 1 ,则为 fork 节点。
        // 重要:在流程定义解析时,需要判断如果是 fork,则 outcome >=2, income=1; 类似的,如果是 join,则 outcome = 1,income>=2

        ParallelGateway parallelGateway = (ParallelGateway)pvmActivity.getModel();



        ConfigurationOption serviceOrchestrationOption = processEngineConfiguration
            .getOptionContainer().get(ConfigurationOption.SERVICE_ORCHESTRATION_OPTION.getId());

        //此处，针对基于并行网关的服务编排做了特殊优化处理。
        if(serviceOrchestrationOption.isEnabled()){

            fireEvent(context,pvmActivity, EventConstant.ACTIVITY_START);

            ParallelServiceOrchestration parallelServiceOrchestration = context.getProcessEngineConfiguration()
                .getParallelServiceOrchestration();

            parallelServiceOrchestration.orchestrateService(context, pvmActivity);

             //由于这里仅是服务编排，所以这里直接返回`暂停`信号。
            return true;

        } else {

            return processDefaultLogic(context, pvmActivity, parallelGateway);

        }



    }



    private boolean processDefaultLogic(ExecutionContext context, PvmActivity pvmActivity, ParallelGateway parallelGateway) {


        Map<String, PvmTransition> incomeTransitions = pvmActivity.getIncomeTransitions();
        Map<String, PvmTransition> outcomeTransitions = pvmActivity.getOutcomeTransitions();

        int outComeTransitionSize = outcomeTransitions.size();
        int inComeTransitionSize = incomeTransitions.size();

        if (outComeTransitionSize >= 2 && inComeTransitionSize == 1) {
            //fork

            fireEvent(context,pvmActivity, EventConstant.ACTIVITY_START);


            ExecutorService executorService = context.getProcessEngineConfiguration().getExecutorService();
            if(null == executorService){
                //顺序执行fork
                for (Entry<String, PvmTransition> pvmTransitionEntry : outcomeTransitions.entrySet()) {
                    PvmActivity target = pvmTransitionEntry.getValue().getTarget();

                    target.enter(context);
                }
            }else{
                //并发执行fork  算法说明
                // 前置: 在流程定义解析阶段需要知道,所有网关是否配对,并且在解析期间进行校验
                // 当子线程执行结束时,看下该分支是否到达了fork对应的join(考虑到嵌套), 如果所有分支都已经完成(注意事项:检查到达该fork对应的join节点,需要注意嵌套,父join找父fork,子join找子join),
                // 如果在fork主线程中发现都已经完毕(每个子线程当前的最后一个节点是否为对应的join),则调用join节点的enter ; 否则调用返回,等待下一次外部的signal

                ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();
                AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
                ContextFactory contextFactory = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, ContextFactory.class);
                Map<String, String> properties = pvmActivity.getModel().getProperties();
                Set<Entry<String, PvmTransition>> entries = outcomeTransitions.entrySet();

                Long latchWaitTime = acquireLatchWaitTime(context, properties);
                ParallelGatewayConstant.ExecuteStrategy executeStrategy = getExecuteStrategy(properties);
                boolean isSkipTimeout = isSkipTimeout((String) MapUtil.safeGet(properties, ParallelGatewayConstant.SKIP_TIMEOUT_EXCEPTION));


                // 注意: 重新赋值 如果能匹配到自定义的线程池，直接使用。 允许扩展并行网关的3种属性: timeout="300" strategy="any" poolName="poolA" skipTimeoutExp="true"  使用方法详见  ServiceOrchestrationParallelGatewayTest
                executorService = useSpecifiedExecutorServiceIfNeeded(properties, processEngineConfiguration);

                List<PvmActivityTask> pvmActivityTaskList = new ArrayList<PvmActivityTask>(outComeTransitionSize);

                try {
                    //
//                    PvmActivity finalJoinPvmActivity = initMultiTaskRequestAndFindOutJoinActivity(context, contextFactory, pvmActivityTaskList, entries);
//
//                    List<Future<PvmActivity>> futureExecutionResultList = invoke(latchWaitTime, isSkipTimeout, executeStrategy, executorService, pvmActivityTaskList);
//
//                    acquireFutureResult(context, processEngineConfiguration, latchWaitTime, isSkipTimeout, futureExecutionResultList);

//                // 获取第一个成功执行的future
//                Future<PvmActivity> pvmActivityFuture = getSuccessFuture(futureExecutionResultList, isSkipTimeoutExp);
//
//                PvmActivity futureJoinParallelGateWayPvmActivity = null;
//                if(null == pvmActivityFuture) {
//                    // 如果没有找到，只有一种可能就是子任务全超时被cancel了。直接使用finalJoinActivity
//                    futureJoinParallelGateWayPvmActivity = firstJoinParallelGateWayPvmActivity;
//                } else {
//                    // 直接从future中获取join事件节点
//                    futureJoinParallelGateWayPvmActivity = pvmActivityFuture.get();
//                }

//                PvmActivity futureJoinParallelGateWayPvmActivity = finalJoinParallelGateWayPvmActivity;

//                    ActivityBehavior behavior = finalJoinPvmActivity.getBehavior();
//
//                    //模拟正常流程的继续驱动，将继续推进caller thread 执行后续节点。
//                    behavior.leave(context, finalJoinPvmActivity);

                } catch (Exception e) {
                    throw new EngineException(e);
                }



            }

        } else if (outComeTransitionSize == 1 && inComeTransitionSize >= 2) {
            //join 时必须使用分布式锁。
            // update at 2022.10.31 这里的缩粒度不够大,在极端环境下,还是存在数据可见性的问题.
            // 比如说,当这个锁结束后, 外面还需要进行持久化数据. 理论上,另外一个线程进来执行时,可能这个持久化数据还未完成.
            // 所以这里取消掉默认锁,改为建议在生产环境使用使用分布式锁.

//            LockStrategy lockStrategy = context.getProcessEngineConfiguration().getLockStrategy();
//            if(null == lockStrategy){
//                throw new EngineException("LockStrategy must be implemented for ParallelGateway");
//            }

//            String processInstanceId = context.getProcessInstance().getInstanceId();
//            try{
//                lockStrategy.tryLock(processInstanceId,context);

                super.enter(context, pvmActivity);

                Collection<PvmTransition> inComingPvmTransitions = incomeTransitions.values();

                ProcessInstance processInstance = context.getProcessInstance();

                //当前内存中的，新产生的 active ExecutionInstance
                List<ExecutionInstance> executionInstanceListFromMemory = InstanceUtil.findActiveExecution(processInstance);


                //当前持久化介质中中，已产生的 active ExecutionInstance。
                List<ExecutionInstance> executionInstanceListFromDB =  executionInstanceStorage.findActiveExecution(processInstance.getInstanceId(), super.processEngineConfiguration);

                LOGGER.debug("ParallelGatewayBehavior Joined, the  value of  executionInstanceListFromMemory, executionInstanceListFromDB   is {} , {} ",executionInstanceListFromMemory,executionInstanceListFromDB);



                //Merge 数据库中和内存中的EI。如果是 custom模式，则可能会存在重复记录，所以这里需要去重。 如果是 DataBase 模式，则不会有重复的EI.

                List<ExecutionInstance> mergedExecutionInstanceList = new ArrayList<ExecutionInstance>(executionInstanceListFromMemory.size());


                for (ExecutionInstance instance : executionInstanceListFromDB) {
                    if (executionInstanceListFromMemory.contains(instance)){
                        //ignore
                    }else {
                        mergedExecutionInstanceList.add(instance);
                    }
                }


                mergedExecutionInstanceList.addAll(executionInstanceListFromMemory);

                int reachedJoinCounter = 0;
                List<ExecutionInstance> chosenExecutionInstanceList = new ArrayList<ExecutionInstance>(executionInstanceListFromMemory.size());

                if(null != mergedExecutionInstanceList){

                    for (ExecutionInstance executionInstance : mergedExecutionInstanceList) {

                        if (executionInstance.getProcessDefinitionActivityId().equals(parallelGateway.getId())) {
                            reachedJoinCounter++;
                            chosenExecutionInstanceList.add(executionInstance);
                        }
                    }
                }


                int countOfTheJoinLatch = inComingPvmTransitions.size();

                LOGGER.debug("chosenExecutionInstanceList , reachedJoinCounter,countOfTheJoinLatch  is {} , {} , {} ",chosenExecutionInstanceList,reachedJoinCounter,countOfTheJoinLatch);

                if(reachedJoinCounter == countOfTheJoinLatch){
                    //把当前停留在join节点的执行实例全部complete掉,然后再持久化时,会自动忽略掉这些节点。

                    if(null != chosenExecutionInstanceList){
                        for (ExecutionInstance executionInstance : chosenExecutionInstanceList) {
                            MarkDoneUtil.markDoneExecutionInstance(executionInstance,executionInstanceStorage,
                                processEngineConfiguration);
                        }
                    }

                    return false;

                }else{
                    //未完成的话,流程继续暂停
                    return true;
                }

//            }finally {
//
//                lockStrategy.unLock(processInstanceId,context);
//            }

        }else{
            throw new EngineException("should not touch here:"+pvmActivity);
        }

        return true;
    }




}
