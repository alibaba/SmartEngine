package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import java.util.*;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.CommonGatewayHelper;
import com.alibaba.smart.framework.engine.common.util.InstanceUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.*;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
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

        int inComeTransitionSize = incomeTransitions.size();

        if (CommonGatewayHelper.isForkGateway(pvmActivity)) {
            //fork
            super.enter(context, pvmActivity);

            // TUNE 这里不太优雅,本来应该在execute方法中返回false的,但是execute的返回值是void,大意了. 暂时先不改了,否则很可能影响现有的用户
            // 此外,目前这个类绕过了execute和leave的执行,后面有机会在优化 (并行网关这个类很特殊,既承担了fork又承担了join职责)
            context.getExecutionInstance().setActive(false);
            fireEvent(context,pvmActivity, EventConstant.ACTIVITY_START);

            Collection<PvmTransition> values = pvmActivity.getOutcomeTransitions().values();

            CommonGatewayHelper.leaveAndConcurrentlyForkIfNeeded(context, pvmActivity,values);

        } else if (CommonGatewayHelper.isJoinGateway(pvmActivity)) {

            ProcessInstance processInstance = context.getProcessInstance();

            //这个同步很关键,避免多线程同时进入临界区,然后在下面的逻辑里去创建新的 join ei,然后和countOfTheJoinLatch 进行比较
            synchronized (processInstance){

                super.enter(context, pvmActivity);

                Collection<PvmTransition> inComingPvmTransitions = incomeTransitions.values();

                //当前内存中的，新产生的 active ExecutionInstance

                //当前持久化介质中中，已产生的 active ExecutionInstance。
                List<ExecutionInstance> activeExecutionList =  executionInstanceStorage.findActiveExecution(processInstance.getInstanceId(), super.processEngineConfiguration);


                int countOfTheJoinLatch = inComingPvmTransitions.size();


                //Merge 数据库中和内存中的EI。如果是 custom模式，则可能会存在重复记录(因为custom也是从内存中查询)，所以这里需要去重。 如果是 DataBase 模式，则不会有重复的EI.
                return super.doa(context, parallelGateway, processInstance, activeExecutionList, countOfTheJoinLatch, null);
            }

        }else{
            throw new EngineException("Unexpected behavior: "+pvmActivity);
        }

        return true;
    }

    private boolean common(ParallelGateway parallelGateway, ProcessInstance processInstance, List<ExecutionInstance> executionInstanceListFromDB, int countOfTheJoinLatch) {
        List<ExecutionInstance> executionInstanceListFromMemory = InstanceUtil.findActiveExecution(processInstance);

        List<ExecutionInstance> mergedExecutionInstanceList = new ArrayList<ExecutionInstance>(executionInstanceListFromMemory.size());

        LOGGER.debug("ParallelGatewayBehavior Joined, the  value of  executionInstanceListFromMemory, executionInstanceListFromDB   is {} , {} ",executionInstanceListFromMemory,executionInstanceListFromDB);

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


        LOGGER.debug("chosenExecutionInstanceList , reachedJoinCounter,countOfTheJoinLatch  is {} , {} , {} ",chosenExecutionInstanceList,reachedJoinCounter, countOfTheJoinLatch);

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
    }


}
