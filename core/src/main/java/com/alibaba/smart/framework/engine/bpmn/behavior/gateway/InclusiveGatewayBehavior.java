package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.InclusiveGateway;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.ExclusiveGatewayBehaviorHelper;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.CommonGatewayHelper;
import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.InstanceUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.EventConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;


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

    private boolean innerEnter(ExecutionContext context, PvmActivity pvmActivity, InclusiveGateway inclusiveGateway) {

        if (CommonGatewayHelper.isForkGateway(pvmActivity)) {
            //fork ,在 leave 阶段，再根据配置决定是否并发创建 pvmActivity
            super.enter(context, pvmActivity);

            return false;


        } else if (CommonGatewayHelper.isJoinGateway(pvmActivity)) {

            ProcessInstance processInstance = context.getProcessInstance();

            //这个同步很关键,避免多线程同时进入临界区
            synchronized (processInstance){

                super.enter(context, pvmActivity);

                Map<String, PvmTransition> incomeTransitionsFromJoinGateway = pvmActivity.getIncomeTransitions();





                // 算法说明： 增加 blockId 字段，在 fork 环节时设置好；然后为本 fork 网关后续的每个 ei 设置 blockId （相同的blockId 标识他们在一个 fork-join 内 ，但是 unbalanced 除外 ），
                // 这里的 blockId 为 fork gateway 的 instanceId ；
                // 然后根据 blockId, 依次计算 fork gateway 的直接 outcoming transitions (需要从 join 环节反推，因为存在 unbalanced gateway) ,然后再减去未被激活的 transitions ，也就是 missed transitions

                // 不变式：countOfTheJoinLatch =  inComingPvmTransitions.size() -  missedPvmTransitions (因未满足条件进而未被触发的分支)
                // missedPvmTransitions = 根据 parentExecutionInstanceId (需要在 fork 时，将parentExecutionInstanceId 正确赋值) 查询到包容网关的outgoingTransitions 的直接环节 id，然后判断历史 executionId 是否包含。 如果不包含，则missedPvmTransitions 递增 1
                // reachedJoinCounter 为 进入到本包容网关的内存对象和 db 对象；

                // 如果reachedJoinCounter < countOfTheJoinLatch ，则继续等待
                // 如果reachedJoinCounter == countOfTheJoinLatch ，则触发 join 动作完成，驱动流程继续流转；
                // 如果reachedJoinCounter > countOfTheJoinLatch， 则报错



                ExecutionInstance joinedExecutionInstanceOfInclusiveGateway = context.getExecutionInstance();
                ExecutionInstance forkedExecutionInstanceOfInclusiveGateway = executionInstanceStorage.find(joinedExecutionInstanceOfInclusiveGateway.getBlockId(), processEngineConfiguration);
                if(null == forkedExecutionInstanceOfInclusiveGateway ){
                    // 说明此时还没落库（SE 目前的设计是一次调用链结束后，才会在最后时刻落库。 这个时候需要从内存中查询）
                    ExecutionInstance matchedExecutionInstance = context.getProcessInstance().getActivityInstances().stream()
                        .flatMap(activityInstance -> activityInstance.getExecutionInstanceList().stream())
                        .filter(executionInstance -> executionInstance.getInstanceId().equals(joinedExecutionInstanceOfInclusiveGateway.getBlockId()))
                        .findFirst()
                        .orElseThrow(() -> new EngineException("No forkedExecutionInstanceOfInclusiveGateway found：" + joinedExecutionInstanceOfInclusiveGateway.getBlockId()));
                    
                    forkedExecutionInstanceOfInclusiveGateway = matchedExecutionInstance;
                }


                // from db
                List<ExecutionInstance> allExecutionInstanceList =  executionInstanceStorage.findAll(processInstance.getInstanceId(), super.processEngineConfiguration);
                if(CollectionUtil.isEmpty(allExecutionInstanceList)){
                    // 说明此时还没落库（SE 目前的设计是一次调用链结束后，才会在最后时刻落库。 这个时候需要从内存中查询）
                    allExecutionInstanceList = context.getProcessInstance().getActivityInstances().stream()
                            .flatMap(activityInstance -> activityInstance.getExecutionInstanceList().stream()).collect(Collectors.toList());
                }


                PvmProcessDefinition pvmProcessDefinition = processEngineConfiguration
                        .getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,
                                ProcessDefinitionContainer.class).getPvmProcessDefinition(processInstance.getProcessDefinitionId(),
                                processInstance.getProcessDefinitionVersion());

                PvmActivity forkPvmActivity = pvmProcessDefinition.getActivities().get(forkedExecutionInstanceOfInclusiveGateway.getProcessDefinitionActivityId());



                int countOfTheJoinLatch = 0; //fixme


                // activityIdList 是流程定义中，join配对的fork轨迹内部的所有的 activityId （由于存在 unbalanced gateway，会有 1个 fork，2个 join 这种情况  ）（与具体的流程实例无关）
                List<String> activityIdList = new ArrayList<>();
                String forkInclusiveGateWayActivityId = forkPvmActivity.getModel().getId();

                //tune maybe cache
                collectActivityIdBetweenForkJoinInclusiveGatewayRecursively(incomeTransitionsFromJoinGateway, forkInclusiveGateWayActivityId, activityIdList);


//                List<String> maximumLatchInTheory = calcLatch(forkPvmActivity, activityIdList);


                for (ExecutionInstance executionInstance : allExecutionInstanceList) {
                    for (String activityId : activityIdList) {
                        if(activityId.equals(executionInstance.getProcessDefinitionActivityId())){
                            // 完成整个循环后，countOfTheJoinLatch 就初始化完毕了
                            countOfTheJoinLatch++;
                            break;
                        }
                    }

                }


                // 后续就是计算 reachedJoinCounter 与 countOfTheJoinLatch 之间的简单比较了


                //当前内存中的，新产生的 active ExecutionInstance
                List<ExecutionInstance> executionInstanceListFromMemory = InstanceUtil.findActiveExecution(processInstance);

                List<ExecutionInstance> activeExecutionListFromDB =   allExecutionInstanceList.stream()
                        .filter(ExecutionInstance::isActive).collect(Collectors.toList());


                //测试场景： 1 . 不嵌套（都触发 ，都不触发， 触发 1,2,3 ，all service; service+receiver, service+userTask ）  2. 不嵌套，但是unbalanced  3. 嵌套， 1大2小
                // 都不满足后的 default 生效 ； 是否并发或者非并发
                // InclusiveGateway only works in DataBase model

                List<ExecutionInstance> mergedExecutionInstanceList = new ArrayList<ExecutionInstance>(executionInstanceListFromMemory.size());


                for (ExecutionInstance instance : activeExecutionListFromDB) {
                    if (executionInstanceListFromMemory.contains(instance)){
                        //todo logger error
                    }else {
                        mergedExecutionInstanceList.add(instance);
                    }
                }


                mergedExecutionInstanceList.addAll(executionInstanceListFromMemory);

                int reachedJoinCounter = 0;
                List<ExecutionInstance> chosenExecutionInstanceList = new ArrayList<ExecutionInstance>(executionInstanceListFromMemory.size());

                if(null != mergedExecutionInstanceList){

                    for (ExecutionInstance executionInstance : mergedExecutionInstanceList) {

                        if (executionInstance.getProcessDefinitionActivityId().equals(inclusiveGateway.getId())) {
                            reachedJoinCounter++;
                            chosenExecutionInstanceList.add(executionInstance);
                        }
                    }
                }



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

    }

    private static List<String> calcLatch(PvmActivity forkPvmActivity, List<String> activityIdList) {
        //此时maximumLatchInTheory 是本 fork 网关 对应的直接 outcoming 环节 id list ，还需要去除掉为未触发的分支，也就是 missed transition

        List<String> maximumLatchInTheory = new ArrayList<>();

        Map<String, PvmTransition> outcomeTransitions = forkPvmActivity.getOutcomeTransitions();

        for (String processDefinitionActivityId : activityIdList) {

            for (Map.Entry<String, PvmTransition> entry : outcomeTransitions.entrySet()) {

                if(processDefinitionActivityId.equals(entry.getKey())){
                    maximumLatchInTheory.add(processDefinitionActivityId);
                    break;
                }
            }
        }
        return maximumLatchInTheory;
    }

    private static void collectActivityIdBetweenForkJoinInclusiveGatewayRecursively(Map<String, PvmTransition> incomeTransitionsFromJoinGateway, String id, List<String> activityIdList) {
        for (Map.Entry<String, PvmTransition> entry : incomeTransitionsFromJoinGateway.entrySet()) {
            PvmTransition value = entry.getValue();
            PvmActivity source = value.getSource();

            String activityId = source.getModel().getId();
            if(!activityId.equals(id)){
                activityIdList.add(activityId);
                collectActivityIdBetweenForkJoinInclusiveGatewayRecursively(source.getIncomeTransitions(), id,activityIdList);
            }else {
                break;
            }

        }
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
