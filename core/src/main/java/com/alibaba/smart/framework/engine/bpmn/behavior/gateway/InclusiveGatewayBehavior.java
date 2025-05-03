package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import com.alibaba.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.InclusiveGateway;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper.CommonGatewayHelper;
import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.tree.ActivityTreeNode;
import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
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

/**
 * 代码逻辑整体说明:
 *   从功能上讲,包容网关类似于并行网关,只不过叠加了类似互斥网关的特性,是否触发对应的分支,是由对应的Transition上的表达式决定的.
 *   另外,看到了有些开源引擎支持了 unbalanced inclusiveGateway特性的, 所以我也决定支持下.
 *
 *   从实现逻辑上来讲,有几个难点:
 *   1. 包容网关的 fork 和 join 节点都是xml 里面的 inclusiveGateway 节点,所以天然需要区分,但是职责上还是违反了 SRP 的.
 *   2. 因为触发对应的分支是由表达式条件决定的,所以必须在 fork 网关 leave 时,记录下 triggered activityIds
 *   3. 由于存在unbalanced 和 embedded 这两种情况存在 ,所以不能简单的在 join 环节确定对应的 fork 的triggered activityIds.
 *   4. 由于通常在子线程运行 fork 逻辑,而子线程通常无法简单获得主线程未提交的数据,所以需要额外处理
 *   5.
 */
@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = InclusiveGateway.class)
public class InclusiveGatewayBehavior extends AbstractActivityBehavior<InclusiveGateway> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InclusiveGatewayBehavior.class);
    public static final String TRIGGER_ACTIVITY_IDS = "$triggerActivityIds$";


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
            return;
        }

        // 重要,在 fork 环节,设置了context#BlockId, 这样后续所有 EI 都会打上这个标记. 便于后续在 join 环节找到对应的 fork (主要服务于 unbalanced gateway)
        String instanceId = context.getExecutionInstance().getInstanceId();
        context.setBlockId(instanceId);

    }


    private boolean innerEnter(ExecutionContext context, PvmActivity pvmActivity, InclusiveGateway inclusiveGateway) {

        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();

        if (CommonGatewayHelper.isForkGateway(pvmActivity)) {
            //先简单处理 ,然后重点是在 leave 阶段，根据触发条件表达式和相关配置, 决定是否并发创建 pvmActivity
            super.enter(context, pvmActivity);

            return false;


        } else if (CommonGatewayHelper.isJoinGateway(pvmActivity)) {

            return handleJoinGatewayEnter(context, pvmActivity, inclusiveGateway, variablePersister);

        }else{
            throw new EngineException("Unexpected behavior: "+pvmActivity);
        }

    }

    private boolean handleJoinGatewayEnter(ExecutionContext context, PvmActivity joinedPvmActivity, InclusiveGateway inclusiveGateway, VariablePersister variablePersister) {
        ProcessInstance processInstance = context.getProcessInstance();

        //这个同步很关键,避免多线程同时进入临界区
        synchronized (processInstance){

            super.enter(context, joinedPvmActivity);

            // 然后根据 blockId, 依次计算 fork gateway 的直接 outcoming transitions (需要从 join 环节反推，因为存在 unbalanced gateway) ,然后再减去未被激活的 transitions ，也就是 missed transitions

            // 不变式：countOfTheJoinLatch =  inComingPvmTransitions.size() -  missedPvmTransitions (因未满足条件进而未被触发的分支)

            // 如果reachedJoinCounter < countOfTheJoinLatch ，则继续等待
            // 如果reachedJoinCounter == countOfTheJoinLatch ，则触发 join 动作完成，驱动流程继续流转；
            // 如果reachedJoinCounter > countOfTheJoinLatch， 则报错


            ExecutionInstance joinedExecutionInstanceOfInclusiveGateway = context.getExecutionInstance();

            // 算法说明： 增加 blockId 字段，在 fork 环节完成设置；然后为本 fork 网关后续的每个 ei 设置 blockId （相同的blockId 标识他们在一个 fork-join 内 ） .这里的 blockId 为 fork gateway 的 instanceId ；
            ExecutionInstance forkedExecutionInstanceOfInclusiveGateway = findForkedExecutionInstance(context, joinedExecutionInstanceOfInclusiveGateway);
            PvmActivity forkedPvmActivity = getForkPvmActivity(processInstance, forkedExecutionInstanceOfInclusiveGateway);

            List<ExecutionInstance> allExecutionInstanceList = calcAllExecutionInstances(context, processInstance);

            // activityIdList 是流程定义中，join配对的fork轨迹内部的所有的 activityId （由于存在 unbalanced gateway，会有 1个 fork，2个 join 这种情况  ）（与具体的流程实例无关）
            ActivityTreeNode activityTreeNode = buildActivityTreeFromJoinToFork(forkedPvmActivity, joinedPvmActivity);

            List<String> triggerActivityIds = calcTriggerActivityIds(context,variableInstanceStorage, forkedExecutionInstanceOfInclusiveGateway, variablePersister);


            int     countOfTheJoinLatch = calcCountOfTheJoinLatch(activityTreeNode, triggerActivityIds);

            //当前内存中的，新产生的 active ExecutionInstance
            List<ExecutionInstance> activeExecutionList =   allExecutionInstanceList.stream()
                    .filter(ExecutionInstance::isActive).collect(Collectors.toList());

            return super.doa(context, inclusiveGateway, processInstance, activeExecutionList, countOfTheJoinLatch, forkedExecutionInstanceOfInclusiveGateway);
        }
    }

    protected    void hookCleanUp(ExecutionContext context, ExecutionInstance forkedExecutionInstanceOfInclusiveGateway) {
        if(null != forkedExecutionInstanceOfInclusiveGateway.getBlockId()){
            // 说明 forkedExecutionInstanceOfInclusiveGateway 是个嵌套网关,需要手动更新 context 的 blockId,然后方便后续join 网关识别出对应的 fork
            context.setBlockId(forkedExecutionInstanceOfInclusiveGateway.getBlockId());

        }
    }


    private List<String> calcTriggerActivityIds(ExecutionContext context,VariableInstanceStorage variableInstanceStorage, ExecutionInstance forkedExecutionInstanceOfInclusiveGateway, VariablePersister variablePersister) {
        // 需要解释下,在通常情况下, Java 子线程是无法直接获得主线程的未提交的数据.
        // 所以,在某些情况下,variableInstance 这个数据是拿不到的(因为某些流程实例中间流程是不暂停的,主线程的数据没有及时落库)
        // 基于上述分析,假设是不暂停流程,则可以从 context 里面去获取 ;如果为空,则说明是暂停型流程,并且此时主线程的数据应该已经落库. (如果有其他情况再说)

        String processDefinitionActivityId = forkedExecutionInstanceOfInclusiveGateway.getProcessDefinitionActivityId();

        Map<String, Object> innerExtra = context.getInnerExtra();
        String key = buildTriggeredActivityIdKey(processDefinitionActivityId);

        if(MapUtil.isNotEmpty(innerExtra)){
                List<String> triggerActivityIds = (List<String>)innerExtra.get(key);
                if(CollectionUtil.isNotEmpty(triggerActivityIds)){
                    return triggerActivityIds;
                }

        }

        return findTriggerActivityIdsFromDB(variableInstanceStorage, forkedExecutionInstanceOfInclusiveGateway, variablePersister, key);
    }

    private List<String> findTriggerActivityIdsFromDB(VariableInstanceStorage variableInstanceStorage, ExecutionInstance forkedExecutionInstanceOfInclusiveGateway, VariablePersister variablePersister, String key) {
        List<VariableInstance> list = variableInstanceStorage.findList(forkedExecutionInstanceOfInclusiveGateway.getProcessInstanceId(), forkedExecutionInstanceOfInclusiveGateway.getInstanceId(), variablePersister, processEngineConfiguration);
        Optional<VariableInstance> first = list.stream().filter(variableInstance -> key.equals(variableInstance.getFieldKey())).findFirst();

        VariableInstance variableInstance = first.get();
        String fieldValue = (String) variableInstance.getFieldValue();

        //本流程实例的实际触发的 ActivityIds
        List<String> triggerActivityIds =( List<String>) variablePersister.deserialize(variableInstance.getFieldKey(),variableInstance.getFieldType().getName(),fieldValue);
        return triggerActivityIds;
    }

    private static String buildTriggeredActivityIdKey(String processDefinitionActivityId) {
        return processDefinitionActivityId + ":" + TRIGGER_ACTIVITY_IDS;
    }

    private static ActivityTreeNode buildActivityTreeFromJoinToFork(PvmActivity forkedPvmActivity, PvmActivity joinedPvmActivity) {
        String joinGatewayActivityId = joinedPvmActivity.getModel().getId();
        ActivityTreeNode root = new ActivityTreeNode(joinGatewayActivityId);
        String forkGatewayActivityId = forkedPvmActivity.getModel().getId();
        buildActivityTree(joinedPvmActivity.getIncomeTransitions(), forkGatewayActivityId, root);
        return root;
    }

    private static void buildActivityTree(Map<String, PvmTransition> transitions, String forkGatewayActivityId, ActivityTreeNode parent) {
        for (Map.Entry<String, PvmTransition> entry : transitions.entrySet()) {
            PvmTransition transition = entry.getValue();
            PvmActivity source = transition.getSource();
            String activityId = source.getModel().getId();
            
            if (activityId.equals(forkGatewayActivityId)) {
                // 找到fork节点,停止遍历
                return;
            }
            
            ActivityTreeNode node = new ActivityTreeNode(activityId);
            parent.addChild(node);
            // 继续向上遍历source节点的income transitions
            buildActivityTree(source.getIncomeTransitions(), forkGatewayActivityId, node);
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

    @Override
    public void leave(ExecutionContext context, PvmActivity pvmActivity) {

        if (CommonGatewayHelper.isForkGateway(pvmActivity)) {
            fireEvent(context,pvmActivity, EventConstant.ACTIVITY_END);

            //fork
            List<PvmTransition> matchedTransitions = CommonGatewayHelper.calcMatchedTransitions(pvmActivity, context);

            putTriggerActivityIdsAndKeepForkContext(  pvmActivity,context, matchedTransitions);

            List<String> triggerActivityIds = (List<String>)context.getInnerExtra().get(buildTriggeredActivityIdKey(pvmActivity.getModel().getId()));

            // 暂停型流程实例,会丢失掉内存中的 context 数据,所以这里仍然需要持久化.
            persistMatchedTransitionsEagerly(  pvmActivity,context,  triggerActivityIds);

            CommonGatewayHelper.leaveAndConcurrentlyForkIfNeeded(context, pvmActivity,matchedTransitions);

        } else if (CommonGatewayHelper.isJoinGateway(pvmActivity)) {

            super.leave(context,pvmActivity);

        }

    }

    private static void putTriggerActivityIdsAndKeepForkContext(PvmActivity pvmActivity, ExecutionContext context, List<PvmTransition> matchedTransitions) {
        List<String> triggerActivityIds = matchedTransitions.stream().map(pvmTransition -> pvmTransition.getTarget()).map(activity -> activity.getModel().getId()).collect(Collectors.toList());

        if(MapUtil.isEmpty(context.getInnerExtra())){
            HashMap<String, Object> extra = new HashMap<>();
            context.setInnerExtra(extra);
        }

        String id = pvmActivity.getModel().getId();

        String key = buildTriggeredActivityIdKey(id);

        context.getInnerExtra().put(key,triggerActivityIds);

    }

    private void persistMatchedTransitionsEagerly(PvmActivity pvmActivity,ExecutionContext context,List<String> triggerTransitions) {

        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();

        VariableInstance variableInstance = new DefaultVariableInstance();
        processEngineConfiguration.getIdGenerator().generate(variableInstance);
        ProcessInstance processInstance = context.getProcessInstance();
        variableInstance.setProcessInstanceId(processInstance.getInstanceId());
        ExecutionInstance executionInstance = context.getExecutionInstance();
        variableInstance.setExecutionInstanceId(executionInstance.getInstanceId());

        variableInstance.setFieldKey(buildTriggeredActivityIdKey(pvmActivity.getModel().getId()));
        variableInstance.setFieldType(String.class);
        variableInstance.setFieldValue(variablePersister.serialize(triggerTransitions));

        variableInstanceStorage.insert(variablePersister,variableInstance, processEngineConfiguration);

    }

}
