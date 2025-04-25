package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.InclusiveGateway;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.ExclusiveGatewayBehaviorHelper;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.CommonGatewayHelper;
import com.alibaba.smart.framework.engine.common.util.InstanceUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
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

import java.util.*;


@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = InclusiveGateway.class)
public class InclusiveGatewayBehavior extends AbstractActivityBehavior<InclusiveGateway> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InclusiveGatewayBehavior.class);


    public InclusiveGatewayBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {

        InclusiveGateway inclusiveGateway = (InclusiveGateway)pvmActivity.getModel();

        return innerEnter(context, pvmActivity, inclusiveGateway);

    }

    private boolean innerEnter(ExecutionContext context, PvmActivity pvmActivity, InclusiveGateway InclusiveGateway) {

        if (CommonGatewayHelper.isForkGateway(pvmActivity)) {
            //fork ,在 leave 阶段，再根据配置决定是否并发创建 pvmActivity
            super.enter(context, pvmActivity);

        } else if (CommonGatewayHelper.isJoinGateway(pvmActivity)) {

            ProcessInstance processInstance = context.getProcessInstance();

            //这个同步很关键,避免多线程同时进入临界区
            synchronized (processInstance){

                super.enter(context, pvmActivity);
                Map<String, PvmTransition> incomeTransitions = pvmActivity.getIncomeTransitions();

                Collection<PvmTransition> inComingPvmTransitions = incomeTransitions.values();

                // 不变式：countOfTheJoinLatch =  inComingPvmTransitions.size() -  missedPvmTransitions (因未满足条件进而未被触发的分支)
                // missedPvmTransitions = 根据 parentExecutionInstanceId (需要在 fork 时，将parentExecutionInstanceId 正确赋值) 查询到包容网关的outgoingTransitions 的直接环节 id，然后判断历史 executionId 是否包含。 如果不包含，则missedPvmTransitions 递增 1
                // reachedJoinCounter 为 进入到本包容网关的内存对象和 db 对象；

                // 如果reachedJoinCounter < countOfTheJoinLatch ，则继续等待
                // 如果reachedJoinCounter == countOfTheJoinLatch ，则触发 join 动作完成，驱动流程继续流转；
                // 如果reachedJoinCounter > countOfTheJoinLatch， 则报错

                //当前内存中的，新产生的 active ExecutionInstance
                List<ExecutionInstance> executionInstanceListFromMemory = InstanceUtil.findActiveExecution(processInstance);

                List<ExecutionInstance> allExecutionInstanceList =  executionInstanceStorage.findAll(processInstance.getInstanceId(), super.processEngineConfiguration);





                LOGGER.debug("InclusiveGatewayBehavior Joined, the  value of  executionInstanceListFromMemory, executionInstanceListFromDB   is {} , {} ",executionInstanceListFromMemory,allExecutionInstanceList);

                // InclusiveGateway only works in DataBase model

                List<ExecutionInstance> mergedExecutionInstanceList = new ArrayList<ExecutionInstance>(executionInstanceListFromMemory.size());


                for (ExecutionInstance instance : allExecutionInstanceList) {
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

            CommonGatewayHelper.leave(context, pvmActivity,matchedTransitions);

        } else if (CommonGatewayHelper.isJoinGateway(pvmActivity)) {

            super.leave(context,pvmActivity);

        }


    }


}
