package com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper;

import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.AbstractGateway;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.configuration.ExceptionProcessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.PvmActivityTask;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.ParallelGatewayConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.ContextFactory;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import java.util.*;
import java.util.concurrent.*;

import static com.alibaba.smart.framework.engine.util.ParallelGatewayUtil.*;
import static com.alibaba.smart.framework.engine.util.ParallelGatewayUtil.hasValidLatchWaitTime;
import static com.alibaba.smart.framework.engine.util.ParallelGatewayUtil.invoke;
import static com.alibaba.smart.framework.engine.util.ParallelGatewayUtil.useSpecifiedExecutorServiceIfNeeded;


public abstract class CommonGatewayHelper {


    // 判断是否为 Fork 网关
    // 如果是 fork,则 outcome >=2, income=1; 类似的,如果是 join,则 outcome = 1,income>=2
    public static boolean isForkGateway(PvmActivity pvmActivity) {

        int inComeTransitionSize = pvmActivity.getIncomeTransitions().size();
        int outComeTransitionSize = pvmActivity.getOutcomeTransitions().size();
        return inComeTransitionSize == 1 && outComeTransitionSize > 1;
    }

    // 判断是否为 Join 网关
    public static boolean isJoinGateway( PvmActivity pvmActivity) {

        int inComeTransitionSize = pvmActivity.getIncomeTransitions().size();
        int outComeTransitionSize = pvmActivity.getOutcomeTransitions().size();
        return inComeTransitionSize > 1 &&  outComeTransitionSize == 1;
    }


    /**
     * 仅支持 balanced gateway
     * @param pvmProcessDefinition
     * @return
     */
    public static <T extends AbstractGateway> Map<String,String> findMatchedJoinParallelGateway(PvmProcessDefinition pvmProcessDefinition, Class<T> clazz) {
        Map<String,String> resultMap = new HashMap();


        Map<String, PvmActivity> pvmActivityMap = pvmProcessDefinition.getActivities();

        List<T> elementListByType = getElementListByType(pvmActivityMap, clazz);


        for (T parallelGateway : elementListByType) {
            PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(parallelGateway.getId());

            //仅针对fork网关进行处理
            if( isForkGateway(pvmActivity)){

                //如果是子fork节点,那么该节点应该在递归中处理完毕. 这里不用重复处理
                String id = pvmActivity.getModel().getId();
                if(null == resultMap.get(id)){
                    findOutAllForkJoinPairs( pvmActivity,pvmProcessDefinition,resultMap);
                }

            }
        }


        return  resultMap;
    }

    private static void findOutAllForkJoinPairs(PvmActivity forkPvmActivity, PvmProcessDefinition pvmProcessDefinition, Map<String,String> resultMap ) {


        Map<String, PvmTransition> outcomeTransitions = forkPvmActivity.getOutcomeTransitions();

        //针对所有分支处理
        for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {

            PvmTransition pendingTransition = transitionEntry.getValue();
            PvmActivity mayBeJoinTarget = pendingTransition.getTarget();


            mayBeJoinTarget =  filterNonParallelGateway(mayBeJoinTarget,pvmProcessDefinition);

            //  遇到的fork网关 ,说明该分支有嵌套fork,则递归进入
            if(isForkGateway(mayBeJoinTarget)){
                //递归处理
                findOutAllForkJoinPairs(mayBeJoinTarget,pvmProcessDefinition,resultMap);
            } else  if(isJoinGateway(mayBeJoinTarget)){
                resultMap.put(forkPvmActivity.getModel().getId(),mayBeJoinTarget.getModel().getId());
                break;
            }else{
                // do nothing
            }

        }
    }

    private static PvmActivity filterNonParallelGateway(PvmActivity currentPvmActivity, PvmProcessDefinition pvmProcessDefinition){

        //如果当前节点就是ParallelGateway时,则立即返回
        if(isForkGateway(currentPvmActivity) || isJoinGateway(currentPvmActivity)){
            return currentPvmActivity;
        }


        Map<String, PvmTransition> outcomeTransitions = currentPvmActivity.getOutcomeTransitions();

        //针对所有分支处理
        for (Map.Entry<String, PvmTransition> transitionEntry : outcomeTransitions.entrySet()) {

            PvmTransition pendingTransition = transitionEntry.getValue();
            PvmActivity mayBeJoinTarget = pendingTransition.getTarget();

            if (isForkGateway(mayBeJoinTarget) || isJoinGateway(mayBeJoinTarget)){
                //return 只会跳出当前递归调用，而不会跳出整个循环或递归栈。 所以还需要 在else分支中处理下
                return mayBeJoinTarget;
            }else{
                // 如果既不是fork,也不是join,则继续遍历target的后向节点
                // 递归处理,在下一个判断中继续寻找分支上的后续节点(这里可能存在互斥网关,所以需要在递归内遍历所有分支)
                PvmActivity result = filterNonParallelGateway(mayBeJoinTarget, pvmProcessDefinition);
                if (result != null) {
                    // 如果递归调用找到了目标网关，立即返回
                    return result;
                }
            }


        }

        throw new EngineException("should find one parallel gateway :"+ currentPvmActivity.getModel().getId());
    }




    public static <T extends Activity> List<T> getElementListByType(Map<String, PvmActivity> pvmActivityMap,Class<T> elementType) {
        List<T> list = new ArrayList();

        for (Map.Entry<String, PvmActivity> pvmActivityEntry : pvmActivityMap.entrySet()) {
            PvmActivity pvmActivity = pvmActivityEntry.getValue();

            Activity activity = pvmActivity.getModel();

            if(elementType.isInstance(activity)){
                list.add(elementType.cast(activity));
            }
        }

        return list;
    }


    public static void leave(ExecutionContext context, PvmActivity pvmActivity, Collection<PvmTransition> values ) {


        int outComeTransitionSize = values.size();

        ExecutorService executorService = context.getProcessEngineConfiguration().getExecutorService();
        ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        ContextFactory contextFactory = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, ContextFactory.class);

        if(null == executorService){
            //顺序执行fork

            for (PvmTransition value : values) {
                PvmActivity target = value.getTarget();

                ExecutionContext childThreadContext = contextFactory.createGatewayContext(context);

                target.enter(childThreadContext);
            }


        }else{
            //并发执行fork  算法说明
            // 前置: 在流程定义解析阶段需要知道,所有网关是否配对,并且在解析期间进行校验
            // 当子线程执行结束时,看下该分支是否到达了fork对应的join(考虑到嵌套), 如果所有分支都已经完成(注意事项:检查到达该fork对应的join节点,需要注意嵌套,父join找父fork,子join找子join),
            // 如果在fork主线程中发现都已经完毕(每个子线程当前的最后一个节点是否为对应的join),则调用join节点的enter ; 否则调用返回,等待下一次外部的signal



            Map<String, String> properties = pvmActivity.getModel().getProperties();


            Long latchWaitTime = acquireLatchWaitTime(context, properties);
            ParallelGatewayConstant.ExecuteStrategy executeStrategy = getExecuteStrategy(properties);
            boolean isSkipTimeout = isSkipTimeout((String) MapUtil.safeGet(properties, ParallelGatewayConstant.SKIP_TIMEOUT_EXCEPTION));


            // 注意: 重新赋值 如果能匹配到自定义的线程池，直接使用。 允许扩展并行网关的3种属性: timeout="300" strategy="any" poolName="poolA" skipTimeoutExp="true"  使用方法详见  ServiceOrchestrationParallelGatewayTest
            executorService = useSpecifiedExecutorServiceIfNeeded(properties, processEngineConfiguration);

            List<PvmActivityTask> pvmActivityTaskList = new ArrayList<PvmActivityTask>(outComeTransitionSize);

            try {

                initTaskList(context, contextFactory, values, pvmActivityTaskList);


                List<Future<ExecutionContext>> futureExecutionResultList = invoke(latchWaitTime, isSkipTimeout, executeStrategy, executorService, pvmActivityTaskList);

                List<ExecutionContext> subThreadContextList =  acquireResults(context, processEngineConfiguration, latchWaitTime, futureExecutionResultList);


                //这里目前看起来没啥必要了
                for (ExecutionContext executionContext : subThreadContextList) {
                    executionContext.getExecutionInstance().getProcessDefinitionActivityId();
                }


            } catch (Exception e) {
                throw new EngineException(e);
            }



        }
    }

    private static void initTaskList(ExecutionContext context, ContextFactory contextFactory,  Collection<PvmTransition> values, List<PvmActivityTask> taskList) {
        for (PvmTransition value : values) {

            //target 为fork 节点的后继节点，比如service1，service3
            PvmActivity target = value.getTarget();

            //将ParentContext 复制到 子线程内
            ExecutionContext subThreadContext = contextFactory.createGatewayContext(context);

            PvmActivityTask pvmActivityTask = context.getProcessEngineConfiguration().getPvmActivityTaskFactory().create(target,subThreadContext);

            taskList.add(pvmActivityTask);
        }
    }

    private static List<ExecutionContext> acquireResults(ExecutionContext context, ProcessEngineConfiguration processEngineConfiguration, Long latchWaitTime, List<Future<ExecutionContext>> futureExecutionResultList) throws TimeoutException {
        ExceptionProcessor exceptionProcessor = processEngineConfiguration.getExceptionProcessor();
        List<ExecutionContext> subThreadExecutionContextList = new ArrayList<>(futureExecutionResultList.size());
        for (Future<ExecutionContext> future : futureExecutionResultList) {
            try {

                if (hasValidLatchWaitTime(latchWaitTime)) {
                    ExecutionContext subThreadExecutionContext = future.get(latchWaitTime, TimeUnit.MILLISECONDS);
                    subThreadExecutionContextList.add(subThreadExecutionContext);
                } else {
                    ExecutionContext subThreadExecutionContext = future.get();
                    subThreadExecutionContextList.add(subThreadExecutionContext);

                }
            } catch (InterruptedException e) {
                exceptionProcessor.process(e, context);
            } catch (ExecutionException e) {
                exceptionProcessor.process(e, context);
            } catch (CancellationException e) {
                throw e;
            }
        }

        return  subThreadExecutionContextList;
    }


}
