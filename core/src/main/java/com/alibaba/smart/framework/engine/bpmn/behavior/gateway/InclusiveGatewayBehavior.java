package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.InclusiveGateway;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.ExclusiveGatewayBehaviorHelper;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.CommonGatewayHelper;
import com.alibaba.smart.framework.engine.common.util.InstanceUtil;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.*;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
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

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;


@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = InclusiveGateway.class)
public class InclusiveGatewayBehavior extends AbstractActivityBehavior<InclusiveGateway> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InclusiveGatewayBehavior.class);


    public InclusiveGatewayBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {

        //算法说明:InclusiveGatewayBehavior 同时承担 fork 和 join 职责。所以说,如何判断是 fork 还是 join ?
        // 目前主要就看pvmActivity节点的 incomeTransition 和 outcomeTransition 数量差异。
        // 如果 income 为1,则为 join 节点。
        // 如果 outcome 为 1 ,则为 fork 节点。
        // 重要:在流程定义解析时,需要判断如果是 fork,则 outcome >=2, income=1; 类似的,如果是 join,则 outcome = 1,income>=2

        InclusiveGateway inclusiveGateway = (InclusiveGateway)pvmActivity.getModel();

        return innerEnter(context, pvmActivity, inclusiveGateway);

    }

    private boolean innerEnter(ExecutionContext context, PvmActivity pvmActivity, InclusiveGateway InclusiveGateway) {

        if (CommonGatewayHelper.isForkGateway(pvmActivity)) {
            //fork
            super.enter(context, pvmActivity);


        } else if (CommonGatewayHelper.isJoinGateway(pvmActivity)) {

            ProcessInstance processInstance = context.getProcessInstance();

            //这个同步很关键,避免多线程同时进入临界区,然后在下面的逻辑里去创建新的 join ei,然后和countOfTheJoinLatch 进行比较
            synchronized (processInstance){

                super.enter(context, pvmActivity);
                Map<String, PvmTransition> incomeTransitions = pvmActivity.getIncomeTransitions();

                Collection<PvmTransition> inComingPvmTransitions = incomeTransitions.values();

                //当前内存中的，新产生的 active ExecutionInstance
                List<ExecutionInstance> executionInstanceListFromMemory = InstanceUtil.findActiveExecution(processInstance);

                //当前持久化介质中中，已产生的 active ExecutionInstance。
                List<ExecutionInstance> executionInstanceListFromDB =  executionInstanceStorage.findActiveExecution(processInstance.getInstanceId(), super.processEngineConfiguration);

                LOGGER.debug("InclusiveGatewayBehavior Joined, the  value of  executionInstanceListFromMemory, executionInstanceListFromDB   is {} , {} ",executionInstanceListFromMemory,executionInstanceListFromDB);

                //Merge 数据库中和内存中的EI。如果是 custom模式，则可能会存在重复记录(因为custom也是从内存中查询)，所以这里需要去重。 如果是 DataBase 模式，则不会有重复的EI.

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

                        if (executionInstance.getProcessDefinitionActivityId().equals(InclusiveGateway.getId())) {
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
            }

        }else{
            throw new EngineException("Unexpected behavior: "+pvmActivity);
        }

        return true;
    }


    @Override
    public void leave(ExecutionContext context, PvmActivity pvmActivity) {

        if (CommonGatewayHelper.isForkGateway(pvmActivity)) {
            fireEvent(context,pvmActivity, EventConstant.ACTIVITY_END);

            //fork
            List<PvmTransition> matchedTransitions = ExclusiveGatewayBehaviorHelper.getMatchedTransitions(pvmActivity, context);

            CommonGatewayHelper.enterConcurrently(context, pvmActivity,matchedTransitions);

        } else if (CommonGatewayHelper.isJoinGateway(pvmActivity)) {

            super.leave(context,pvmActivity);

        }


    }


}
