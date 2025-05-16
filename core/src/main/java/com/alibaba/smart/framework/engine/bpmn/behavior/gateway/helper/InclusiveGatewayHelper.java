package com.alibaba.smart.framework.engine.bpmn.behavior.gateway.helper;

import com.alibaba.smart.framework.engine.bpmn.behavior.gateway.tree.ActivityTreeNode;
import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.instance.impl.DefaultVariableInstance;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.alibaba.smart.framework.engine.deployment.impl.DefaultProcessDefinitionContainer.ACTIVITY_TREE_CACHE;
import static com.alibaba.smart.framework.engine.deployment.impl.DefaultProcessDefinitionContainer.JOIN_LATCH_COUNT_CACHE;

public class InclusiveGatewayHelper {
    public static final String TRIGGER_ACTIVITY_IDS = "$triggerActivityIds$";
    

    // 添加缓存键生成方法
    private static String generateActivityTreeCacheKey(ProcessInstance processInstance,PvmActivity forkedPvmActivity, PvmActivity joinedPvmActivity) {
        return processInstance.getUniqueProcessDefinitionIdAndVersion() + ":" +forkedPvmActivity.getModel().getId() + ":" + joinedPvmActivity.getModel().getId();
    }

    // 添加 JOIN_LATCH_COUNT 缓存键生成方法
    private static String generateJoinLatchCountCacheKey(ProcessInstance processInstance, ActivityTreeNode node, List<String> triggerActivityIds) {
        // 使用流程实例ID、节点ID和触发的活动ID列表作为缓存键
        String processDefinitionIdAndVersion = processInstance.getUniqueProcessDefinitionIdAndVersion();
        String nodeId = node.getActivityId();
        String triggerIds = triggerActivityIds.stream().sorted().collect(Collectors.joining(":"));
        return processDefinitionIdAndVersion + ":" + nodeId + ":" + triggerIds;
    }



    public static void cacheTriggerActivityIdsToContext(PvmActivity pvmActivity, ExecutionContext context, List<PvmTransition> matchedTransitions) {
        List<String> triggerActivityIds = matchedTransitions.stream().map(pvmTransition -> pvmTransition.getTarget()).map(activity -> activity.getModel().getId()).collect(Collectors.toList());

        if(MapUtil.isEmpty(context.getInnerExtra())){
            HashMap<String, Object> extra = new HashMap<>();
            context.setInnerExtra(extra);
        }

        String id = pvmActivity.getModel().getId();

        String key = buildTriggeredActivityIdKey(id);

        context.getInnerExtra().put(key,triggerActivityIds);

    }

    public  static  void persistMatchedTransitionsEagerly(PvmActivity pvmActivity,ExecutionContext context,List<String> triggerTransitions,ProcessEngineConfiguration processEngineConfiguration,VariableInstanceStorage variableInstanceStorage) {

        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();

        VariableInstance variableInstance = new DefaultVariableInstance();
        variableInstance.setTenantId(context.getTenantId());
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


    public static List<String> calcTriggerActivityIds(ExecutionContext context, VariableInstanceStorage variableInstanceStorage, ExecutionInstance forkedExecutionInstanceOfInclusiveGateway, VariablePersister variablePersister,ProcessEngineConfiguration processEngineConfiguration) {
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

        return findTriggerActivityIdsFromDB(variableInstanceStorage, forkedExecutionInstanceOfInclusiveGateway, variablePersister, key,  processEngineConfiguration);
    }

    private static List<String> findTriggerActivityIdsFromDB(VariableInstanceStorage variableInstanceStorage, ExecutionInstance forkedExecutionInstanceOfInclusiveGateway, VariablePersister variablePersister, String key,ProcessEngineConfiguration processEngineConfiguration) {
        List<VariableInstance> list = variableInstanceStorage.findList(forkedExecutionInstanceOfInclusiveGateway.getProcessInstanceId(),
                forkedExecutionInstanceOfInclusiveGateway.getInstanceId(), variablePersister,forkedExecutionInstanceOfInclusiveGateway.getTenantId(), processEngineConfiguration);
        Optional<VariableInstance> first = list.stream().filter(variableInstance -> key.equals(variableInstance.getFieldKey())).findFirst();

        VariableInstance variableInstance = first.get();
        String fieldValue = (String) variableInstance.getFieldValue();

        //本流程实例的实际触发的 ActivityIds
        List<String> triggerActivityIds =( List<String>) variablePersister.deserialize(variableInstance.getFieldKey(),variableInstance.getFieldType().getName(),fieldValue);
        return triggerActivityIds;
    }

    public static String buildTriggeredActivityIdKey(String processDefinitionActivityId) {
        return processDefinitionActivityId + ":" + TRIGGER_ACTIVITY_IDS;
    }

    public static ActivityTreeNode buildActivityTreeFromJoinToFork(PvmActivity forkedPvmActivity, PvmActivity joinedPvmActivity,ProcessInstance processInstance) {
        // 生成缓存键
        String cacheKey = generateActivityTreeCacheKey(  processInstance,forkedPvmActivity, joinedPvmActivity);
        
        // 检查缓存中是否已存在
        ActivityTreeNode cachedTree = ACTIVITY_TREE_CACHE.get(cacheKey);
        if (cachedTree != null) {
            return cachedTree;
        }
        
        // 缓存中不存在，执行原有逻辑构建树
        String joinGatewayActivityId = joinedPvmActivity.getModel().getId();
        ActivityTreeNode root = new ActivityTreeNode(joinGatewayActivityId);

        String forkGatewayActivityId = forkedPvmActivity.getModel().getId();

        buildActivityTree(joinedPvmActivity.getIncomeTransitions(), forkGatewayActivityId, root);
        
        // 将结果存入缓存
        ACTIVITY_TREE_CACHE.put(cacheKey, root);
        
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

    public static PvmActivity getForkPvmActivity(ProcessInstance processInstance, ExecutionInstance forkedExecutionInstanceOfInclusiveGateway,ProcessEngineConfiguration processEngineConfiguration) {
        PvmProcessDefinition pvmProcessDefinition = processEngineConfiguration
                .getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,
                        ProcessDefinitionContainer.class).getPvmProcessDefinition(processInstance.getProcessDefinitionId(),
                        processInstance.getProcessDefinitionVersion(),processInstance.getTenantId());

        PvmActivity forkPvmActivity = pvmProcessDefinition.getActivities().get(forkedExecutionInstanceOfInclusiveGateway.getProcessDefinitionActivityId());
        return forkPvmActivity;
    }

    public static List<ExecutionInstance> calcAllExecutionInstances(ExecutionContext context ,ExecutionInstanceStorage executionInstanceStorage) {
        ProcessInstance processInstance = context.getProcessInstance();
        ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();

        List<ExecutionInstance> allExecutionInstanceList =  executionInstanceStorage.findAll(processInstance.getInstanceId(),processInstance.getTenantId(), processEngineConfiguration);
        if(CollectionUtil.isEmpty(allExecutionInstanceList)){
            // 说明此时还没落库（SE 目前的设计是一次调用链结束后，才会在最后时刻落库。 这个时候需要从内存中查询）
            allExecutionInstanceList = processInstance.getActivityInstances().stream()
                    .flatMap(activityInstance -> activityInstance.getExecutionInstanceList().stream()).collect(Collectors.toList());
        }
        return allExecutionInstanceList;
    }

    public static ExecutionInstance findForkedExecutionInstance(ExecutionContext context, ExecutionInstance joinedExecutionInstanceOfInclusiveGateway,ExecutionInstanceStorage executionInstanceStorage) {
        ProcessEngineConfiguration processEngineConfiguration = context.getProcessEngineConfiguration();

        ExecutionInstance forkedExecutionInstanceOfInclusiveGateway = executionInstanceStorage.find(
                joinedExecutionInstanceOfInclusiveGateway.getBlockId(),joinedExecutionInstanceOfInclusiveGateway.getTenantId(), processEngineConfiguration);

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


    public static int calcCountOfTheJoinLatch(ActivityTreeNode node, List<String> triggerActivityIds, ProcessInstance processInstance) {
        // 生成缓存键
        String cacheKey = generateJoinLatchCountCacheKey(processInstance, node, triggerActivityIds);
        
        // 检查缓存中是否已存在计算结果
        Integer cachedCount = JOIN_LATCH_COUNT_CACHE.get(cacheKey);
        if (cachedCount != null) {
            return cachedCount;
        }
        
        // 缓存中不存在，执行原有逻辑计算结果
        // 获取所有触发的叶子节点
        List<ActivityTreeNode> triggeredLeafNodes = getTriggeredLeafNodes(node, triggerActivityIds);

        if (triggeredLeafNodes.isEmpty()) {
            JOIN_LATCH_COUNT_CACHE.put(cacheKey, 0);
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

            onlyCollectAncestors(leafId, leaf, ancestorIdSet);

            // 检查是否与已计数的祖先节点有重叠
            boolean hasOverlap = false;

            if(CollectionUtil.isNotEmpty(ancestorIdSet)){
                for (String ancestorId : ancestorIdSet) {
                    if (allCountedAncestorIds.contains(ancestorId)) {
                        hasOverlap = true;
                        break;
                    }
                }

                // 如果没有重叠，则计数+1，并将当前叶子节点的祖先节点ID添加到已计数集合中 (避免因触发同一祖先的两个子节点,而导致重复计数  unbalanced gateway)
                if (!hasOverlap) {
                    count++;
                    allCountedAncestorIds.addAll(ancestorIdSet);
                }
            }else {
                // 去除子节点和 root 节点, ancestorIdSet 为空,那么这种情况下是简单场景
                count++;
            }
        }
        
        // 将计算结果存入缓存
        JOIN_LATCH_COUNT_CACHE.put(cacheKey, count);
        
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
}
