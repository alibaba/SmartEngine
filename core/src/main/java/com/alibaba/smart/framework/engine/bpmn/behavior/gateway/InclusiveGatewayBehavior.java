package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.InclusiveGateway;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.ExclusiveGatewayBehaviorHelper;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.CommonGatewayHelper;
import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.InstanceUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultVariableInstance;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
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
    public static final String INCLUSIVE_GATE_WAY = "inclusiveGateWay";


    public InclusiveGatewayBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {

        // InclusiveGateway only works in DataBase model

        InclusiveGateway inclusiveGateway = (InclusiveGateway)pvmActivity.getModel();

        return innerEnter(context, pvmActivity, inclusiveGateway);

    }

    protected void hookEnter(ExecutionContext context, PvmActivity pvmActivity) {

        if(CommonGatewayHelper.isJoinGateway(pvmActivity)){

//            ExecutionInstance joinedExecutionInstanceOfInclusiveGateway = context.getExecutionInstance();
//            ExecutionInstance forkedExecutionInstanceOfInclusiveGateway = findForkedExecutionInstance(context, joinedExecutionInstanceOfInclusiveGateway);
            return;
        }

        String instanceId = context.getExecutionInstance().getInstanceId();
        context.setBlockId(instanceId);

    }


    private boolean innerEnter(ExecutionContext context, PvmActivity pvmActivity, InclusiveGateway inclusiveGateway) {

        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();
        VariableInstanceStorage variableInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,VariableInstanceStorage.class);

        if (CommonGatewayHelper.isForkGateway(pvmActivity)) {
            //先 fork ,在 leave 阶段，再根据配置决定是否并发创建 pvmActivity
            super.enter(context, pvmActivity);

            return false;


        } else if (CommonGatewayHelper.isJoinGateway(pvmActivity)) {

            ProcessInstance processInstance = context.getProcessInstance();

            //这个同步很关键,避免多线程同时进入临界区
            synchronized (processInstance){

                super.enter(context, pvmActivity);






                // 算法说明： 增加 blockId 字段，在 fork 环节时设置好；然后为本 fork 网关后续的每个 ei 设置 blockId （相同的blockId 标识他们在一个 fork-join 内 ，但是 unbalanced 除外 ），
                // 这里的 blockId 为 fork gateway 的 instanceId ；
                // 然后根据 blockId, 依次计算 fork gateway 的直接 outcoming transitions (需要从 join 环节反推，因为存在 unbalanced gateway) ,然后再减去未被激活的 transitions ，也就是 missed transitions

                // 不变式：countOfTheJoinLatch =  inComingPvmTransitions.size() -  missedPvmTransitions (因未满足条件进而未被触发的分支)

                // 如果reachedJoinCounter < countOfTheJoinLatch ，则继续等待
                // 如果reachedJoinCounter == countOfTheJoinLatch ，则触发 join 动作完成，驱动流程继续流转；
                // 如果reachedJoinCounter > countOfTheJoinLatch， 则报错


                ExecutionInstance joinedExecutionInstanceOfInclusiveGateway = context.getExecutionInstance();
                ExecutionInstance forkedExecutionInstanceOfInclusiveGateway = findForkedExecutionInstance(context, joinedExecutionInstanceOfInclusiveGateway);

//                if(null != forkedExecutionInstanceOfInclusiveGateway.getBlockId()){
//                    // 说明 forkedExecutionInstanceOfInclusiveGateway 是个嵌套网关,需要手动更新 joinEI 的 blockId,然后方便后续join 网关识别出 mainFork
//                    // 虽然没找到更优雅的方式,但是应该解决了问题.
//                    joinedExecutionInstanceOfInclusiveGateway.setBlockId(forkedExecutionInstanceOfInclusiveGateway.getBlockId());
//                    executionInstanceStorage.update(joinedExecutionInstanceOfInclusiveGateway,processEngineConfiguration);
//
//                    // 再次查找 forkedExecutionInstanceOfInclusiveGateway
//                    forkedExecutionInstanceOfInclusiveGateway = findForkedExecutionInstance(context, joinedExecutionInstanceOfInclusiveGateway);
//                }

                List<ExecutionInstance> allExecutionInstanceList = calcAllExecutionInstances(context, processInstance);

                PvmActivity forkedPvmActivity = getForkPvmActivity(processInstance, forkedExecutionInstanceOfInclusiveGateway);

                int countOfTheJoinLatch = 0; //fixme

                // activityIdList 是流程定义中，join配对的fork轨迹内部的所有的 activityId （由于存在 unbalanced gateway，会有 1个 fork，2个 join 这种情况  ）（与具体的流程实例无关）
                ActivityTreeNode activityTreeNode = buildActivityTreeFromJoinToFork(forkedPvmActivity,    pvmActivity);

                List<VariableInstance> list = variableInstanceStorage.findList(forkedExecutionInstanceOfInclusiveGateway.getProcessInstanceId(), forkedExecutionInstanceOfInclusiveGateway.getInstanceId(), variablePersister, processEngineConfiguration);
                Optional<VariableInstance> first = list.stream().filter(variableInstance -> INCLUSIVE_GATE_WAY.equals(variableInstance.getFieldKey())).findFirst();

                VariableInstance variableInstance = first.get();
                String fieldValue = (String) variableInstance.getFieldValue();

                //本流程实例的实际触发的 ActivityIds
                List<String> triggerActivityIds =( List<String>) variablePersister.deserialize(variableInstance.getFieldKey(),variableInstance.getFieldType().getName(),fieldValue);

                // 还需要和 join 对应的 fork 网关 做下 与运算 (因为存在 unbalanced gateway )
//                for (String triggerActivityId : triggerActivityIds) {
//                    for (String activityId : activityIdList) {
//                        if(activityId.equals(triggerActivityId)){
//                            // 完成整个循环后，countOfTheJoinLatch 就初始化完毕了,根据此时的流程实例所有流转轨迹,就能算出countOfTheJoinLatch
//                            countOfTheJoinLatch++;
//                            break;
//                        }
//                    }
//                }

                countOfTheJoinLatch = calcCountOfTheJoinLatch(activityTreeNode, triggerActivityIds);


                // 后续就是计算 reachedJoinCounter 与 countOfTheJoinLatch 之间的简单比较了

                //当前内存中的，新产生的 active ExecutionInstance
                List<ExecutionInstance> executionInstanceListFromMemory = InstanceUtil.findActiveExecution(processInstance);

                List<ExecutionInstance> activeExecutionList =   allExecutionInstanceList.stream()
                        .filter(ExecutionInstance::isActive).collect(Collectors.toList());


                List<ExecutionInstance> mergedExecutionInstanceList = new ArrayList<ExecutionInstance>();

                mergedExecutionInstanceList.addAll(executionInstanceListFromMemory);

                for (ExecutionInstance executionInstance : activeExecutionList) {
                    if(mergedExecutionInstanceList.contains(executionInstance)){
                        //do nothing
                    }else {
                        mergedExecutionInstanceList.add(executionInstance);
                    }
                }

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

                    //clear blockId ,因为这个块的 fork-join 已经结束 todo
//                    context.setBlockId(null);

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

    private static ActivityTreeNode buildActivityTreeFromJoinToFork(PvmActivity forkedPvmActivity, PvmActivity joinedPvmActivity) {
        String joinGatewayActivityId = joinedPvmActivity.getModel().getId();
        ActivityTreeNode root = new ActivityTreeNode(joinGatewayActivityId);
        String forkGatewayActivityId = forkedPvmActivity.getModel().getId();
        buildActivityTree(joinedPvmActivity.getIncomeTransitions(), forkGatewayActivityId, root);
        return root;
    }

    private static void buildActivityTree(Map<String, PvmTransition> transitions, String targetId, ActivityTreeNode parent) {
        for (Map.Entry<String, PvmTransition> entry : transitions.entrySet()) {
            PvmTransition transition = entry.getValue();
            PvmActivity source = transition.getSource();
            String activityId = source.getModel().getId();
            
            if (activityId.equals(targetId)) {
                // 找到fork节点,停止遍历
                return;
            }
            
            ActivityTreeNode node = new ActivityTreeNode(activityId);
            parent.addChild(node);
            // 继续向上遍历source节点的income transitions
            buildActivityTree(source.getIncomeTransitions(), targetId, node);
        }
    }

    private PvmActivity getForkPvmActivity(ProcessInstance processInstance, ExecutionInstance forkedExecutionInstanceOfInclusiveGateway) {
        PvmProcessDefinition pvmProcessDefinition = processEngineConfiguration
                .getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,
                        ProcessDefinitionContainer.class).getPvmProcessDefinition(processInstance.getProcessDefinitionId(),
                        processInstance.getProcessDefinitionVersion());

        PvmActivity forkPvmActivity = pvmProcessDefinition.getActivities().get(forkedExecutionInstanceOfInclusiveGateway.getProcessDefinitionActivityId());
        return forkPvmActivity;
    }

    private List<ExecutionInstance> calcAllExecutionInstances(ExecutionContext context, ProcessInstance processInstance) {
        List<ExecutionInstance> allExecutionInstanceList =  executionInstanceStorage.findAll(processInstance.getInstanceId(), super.processEngineConfiguration);
        if(CollectionUtil.isEmpty(allExecutionInstanceList)){
            // 说明此时还没落库（SE 目前的设计是一次调用链结束后，才会在最后时刻落库。 这个时候需要从内存中查询）
            allExecutionInstanceList = context.getProcessInstance().getActivityInstances().stream()
                    .flatMap(activityInstance -> activityInstance.getExecutionInstanceList().stream()).collect(Collectors.toList());
        }
        return allExecutionInstanceList;
    }

    private ExecutionInstance findForkedExecutionInstance(ExecutionContext context, ExecutionInstance joinedExecutionInstanceOfInclusiveGateway) {
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
        return forkedExecutionInstanceOfInclusiveGateway;
    }


    private static int calcCountOfTheJoinLatch(ActivityTreeNode node, List<String> triggerActivityIds) {
        // 获取所有触发的叶子节点
        List<ActivityTreeNode> triggeredLeafNodes = getTriggeredLeafNodes(node, triggerActivityIds);

        if (triggeredLeafNodes.isEmpty()) {
            return 0;
        }
        
        int count = 0;
        // 用于记录已经计数过的祖先节点ID
        Set<String> allCountedAncestorIds = new HashSet<>();
        
        // 遍历所有触发的叶子节点
        for (ActivityTreeNode leaf : triggeredLeafNodes) {
            // 获取该叶子节点的所有祖先节点ID（包括自身）
            Set<String> ancestorIdSet = new HashSet<>();
            String leafId = leaf.getActivityId();

            onlyCollectAncestors(   leafId,leaf, ancestorIdSet);
            
            // 检查是否与已计数的祖先节点有重叠
            boolean hasOverlap = false;

            if(CollectionUtil.isNotEmpty(ancestorIdSet)){
                for (String ancestorId : ancestorIdSet) {
                    if (allCountedAncestorIds.contains(ancestorId)) {
                        hasOverlap = true;
                        break;
                    }
                }

                // 如果没有重叠，则计数+1，并将当前叶子节点的祖先节点ID添加到已计数集合中
                if (!hasOverlap) {
                    count++;
                    allCountedAncestorIds.addAll(ancestorIdSet);
                }
            }else {
                // 去除子节点和 root 节点, ancestorIdSet 为空,那么这种情况下是简单场景
                count++;

            }

        }
        
        return count;
    }
    
    private static void onlyCollectAncestors(String leafId, ActivityTreeNode node, Set<String> ancestorIds) {

        if(node.getActivityId().equals(leafId)){
            //do nothing
        }else{
            if(node.getParent() == null){
                // 是 root 节点,那么则不放进去. 防止简单场景下 两个分支的父亲节点都是 root 节点
            }else {
                ancestorIds.add(node.getActivityId());
            }
        }

        // 递归添加父节点ID
        ActivityTreeNode parent = node.getParent();
        if (parent != null) {
            onlyCollectAncestors(leafId,parent, ancestorIds);
        }
    }

    private static List<ActivityTreeNode> getTriggeredLeafNodes(ActivityTreeNode node, List<String> triggerActivityIds) {
        // 获取所有叶子节点
        List<ActivityTreeNode> allLeafNodes = new ArrayList<>();

        collectLeafNodes(node, allLeafNodes);

        // 过滤出触发的叶子节点
        List<ActivityTreeNode> triggeredLeafNodes = allLeafNodes.stream()
            .filter(leaf -> triggerActivityIds.contains(leaf.getActivityId()))
            .collect(Collectors.toList());
        return triggeredLeafNodes;
    }

    // 收集所有叶子节点
    private static void collectLeafNodes(ActivityTreeNode node, List<ActivityTreeNode> leafNodes) {
        if (node.getChildren().isEmpty()) {
            leafNodes.add(node);
            return;
        }
        
        for (ActivityTreeNode child : node.getChildren()) {
            collectLeafNodes(child, leafNodes);
        }
    }
//
//    // 收集从根节点到指定节点路径上的所有节点ID
//    private static boolean collectAncestorIds(ActivityTreeNode current, String targetId,
//                                           Set<String> ancestorIds, Set<String> visited) {
//        // 防止循环
//        if (visited.contains(current.getActivityId())) {
//            return false;
//        }
//
//        visited.add(current.getActivityId());
//
//        // 如果找到目标节点
//        if (current.getActivityId().equals(targetId)) {
//            ancestorIds.add(current.getActivityId());
//            return true;
//        }
//
//        // 递归查找子节点
//        for (ActivityTreeNode child : current.getChildren()) {
//            if (collectAncestorIds(child, targetId, ancestorIds, visited)) {
//                // 如果在子树中找到目标节点，则当前节点也是祖先
//                ancestorIds.add(current.getActivityId());
//                return true;
//            }
//        }
//
//        return false;
//    }
//


    @Override
    public void leave(ExecutionContext context, PvmActivity pvmActivity) {

        if (CommonGatewayHelper.isForkGateway(pvmActivity)) {
            fireEvent(context,pvmActivity, EventConstant.ACTIVITY_END);

            //fork
            List<PvmTransition> matchedTransitions = ExclusiveGatewayBehaviorHelper.calcMatchedTransitions(pvmActivity, context);

            persistMatchedTransitionsEagerly(context, matchedTransitions);

            CommonGatewayHelper.leave(context, pvmActivity,matchedTransitions);




        } else if (CommonGatewayHelper.isJoinGateway(pvmActivity)) {

            super.leave(context,pvmActivity);

        }


    }

    private void persistMatchedTransitionsEagerly(ExecutionContext context, List<PvmTransition> matchedTransitions) {
        List<String> collect = matchedTransitions.stream().map(pvmTransition -> pvmTransition.getTarget()).map(activity -> activity.getModel().getId()).collect(Collectors.toList());

        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();
        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();

        VariableInstanceStorage variableInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,VariableInstanceStorage.class);

        VariableInstance variableInstance = new DefaultVariableInstance();
        processEngineConfiguration.getIdGenerator().generate(variableInstance);
        ProcessInstance processInstance = context.getProcessInstance();
        variableInstance.setProcessInstanceId(processInstance.getInstanceId());
        ExecutionInstance executionInstance = context.getExecutionInstance();
        variableInstance.setExecutionInstanceId(executionInstance.getInstanceId());

        variableInstance.setFieldKey(INCLUSIVE_GATE_WAY);
        variableInstance.setFieldType(String.class);
        variableInstance.setFieldValue(variablePersister.serialize(collect));

        variableInstanceStorage.insert(variablePersister,variableInstance, processEngineConfiguration);

    }



//    private static List<String> calcLatch(PvmActivity forkPvmActivity, List<String> activityIdList) {
//        //此时maximumLatchInTheory 是本 fork 网关 对应的直接 outcoming 环节 id list
//        //maximumLatchInTheory 返回了 join 对应的 fork 所有激活的 activityId
//
//        List<String> maximumLatchInTheory = new ArrayList<>();
//
//        Map<String, PvmTransition> outcomeTransitions = forkPvmActivity.getOutcomeTransitions();
//
//        for (String processDefinitionActivityId : activityIdList) {
//
//            for (Map.Entry<String, PvmTransition> entry : outcomeTransitions.entrySet()) {
//
//                if(processDefinitionActivityId.equals(entry.getKey())){
//                    maximumLatchInTheory.add(processDefinitionActivityId);
//                    break;
//                }
//            }
//        }
//        return maximumLatchInTheory;
//    }
//

//
//    private static List<String> calcLatch1(PvmActivity joinActivity, List<String> activityIdList) {
//        //此时maximumLatchInTheory 是本 fork 网关 对应的直接 outcoming 环节 id list ，还需要去除掉为未触发的分支，也就是 missed transition
//
//        List<String> maximumLatchInTheory = new ArrayList<>();
//
//        Map<String, PvmTransition> outcomeTransitions = joinActivity.getIncomeTransitions();
//
//        for (String processDefinitionActivityId : activityIdList) {
//
//            for (Map.Entry<String, PvmTransition> entry : outcomeTransitions.entrySet()) {
//
//                if(processDefinitionActivityId.equals(entry.getKey())){
//                    maximumLatchInTheory.add(processDefinitionActivityId);
//                    break;
//                }
//            }
//        }
//        return maximumLatchInTheory;
//    }
//    private static List<String> calcLatch1(PvmActivity joinActivity, List<String> activityIdList) {
//        //此时maximumLatchInTheory 是本 fork 网关 对应的直接 outcoming 环节 id list ，还需要去除掉为未触发的分支，也就是 missed transition
//
//        List<String> maximumLatchInTheory = new ArrayList<>();
//
//        Map<String, PvmTransition> outcomeTransitions = joinActivity.getIncomeTransitions();
//
//        for (String processDefinitionActivityId : activityIdList) {
//
//            for (Map.Entry<String, PvmTransition> entry : outcomeTransitions.entrySet()) {
//
//                if(processDefinitionActivityId.equals(entry.getKey())){
//                    maximumLatchInTheory.add(processDefinitionActivityId);
//                    break;
//                }
//            }
//        }
//        return maximumLatchInTheory;
//    }

//    private static void collectActivityIdBetweenForkJoinInclusiveGatewayRecursively(Map<String, PvmTransition> incomeTransitionsFromJoinGateway, String id, List<String> activityIdList) {
//        for (Map.Entry<String, PvmTransition> entry : incomeTransitionsFromJoinGateway.entrySet()) {
//            PvmTransition value = entry.getValue();
//            PvmActivity source = value.getSource();
//
//            String activityId = source.getModel().getId();
//            if(!activityId.equals(id)){
//                activityIdList.add(activityId);
//                collectActivityIdBetweenForkJoinInclusiveGatewayRecursively(source.getIncomeTransitions(), id,activityIdList);
//            }else {
//                break;
//            }
//
//        }
//    } private static void collectActivityIdBetweenForkJoinInclusiveGatewayRecursively(Map<String, PvmTransition> incomeTransitionsFromJoinGateway, String id, List<String> activityIdList) {
//        for (Map.Entry<String, PvmTransition> entry : incomeTransitionsFromJoinGateway.entrySet()) {
//            PvmTransition value = entry.getValue();
//            PvmActivity source = value.getSource();
//
//            String activityId = source.getModel().getId();
//            if(!activityId.equals(id)){
//                activityIdList.add(activityId);
//                collectActivityIdBetweenForkJoinInclusiveGatewayRecursively(source.getIncomeTransitions(), id,activityIdList);
//            }else {
//                break;
//            }
//
//        }
//    }

//    private static void calcLachedActivityIds(Map<String, PvmTransition> incomeTransitionsFromJoinGateway) {
//
//        Set<Map.Entry<String, PvmTransition>> entries = incomeTransitionsFromJoinGateway.entrySet();
//
//        List<String> activityIdList = new ArrayList<>(entries.size());
//
//        for (Map.Entry<String, PvmTransition> entry : entries) {
//            PvmTransition value = entry.getValue();
//            PvmActivity source = value.getSource();
//
//            String activityId = source.getModel().getId();
//            activityIdList.add(activityId);
//
//        }
//    }



//                List<String> maximumLatchInTheory = calcLatch(forkedPvmActivity, activityIdList);

    //allExecutionInstanceList 因为延迟落库,然后如果是单线程的情况下,这里allExecutionInstanceList 并不包含等待触发的分支. 所有这么写是有问题的.
//                for (ExecutionInstance executionInstance : allExecutionInstanceList) {
//                    for (String activityId : maximumLatchInTheory) {
//                        if(activityId.equals(executionInstance.getProcessDefinitionActivityId())){
//                            // 完成整个循环后，countOfTheJoinLatch 就初始化完毕了,根据此时的流程实例所有流转轨迹,就能算出countOfTheJoinLatch
//                            countOfTheJoinLatch++;
//                            break;
//                        }
//                    }
//
//                }



}
