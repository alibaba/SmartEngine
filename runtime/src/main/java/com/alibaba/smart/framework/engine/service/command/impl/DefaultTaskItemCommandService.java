package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.TaskItemCompleteProcessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.ProcessInstanceModeConstant;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskItemInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskItemCommandService;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryParam;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public class DefaultTaskItemCommandService implements TaskItemCommandService, LifeCycleListener {

    /**
     * 扩展点注册
     */
    private ExtensionPointRegistry extensionPointRegistry;

    private ProcessInstanceStorage processInstanceStorage;
    private ActivityInstanceStorage activityInstanceStorage;
    private ExecutionInstanceStorage executionInstanceStorage;
    private ExecutionCommandService executionCommandService;
    private ProcessDefinitionContainer processContainer;
    private TaskItemInstanceStorage taskItemInstanceStorage;
    private ExecutionInstanceFactory executionInstanceFactory;


    public DefaultTaskItemCommandService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.executionCommandService = this.extensionPointRegistry.getExtensionPoint(ExecutionCommandService.class);
        this.processInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
        this.activityInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ActivityInstanceStorage.class);
        this.executionInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ExecutionInstanceStorage.class);
        this.processContainer = this.extensionPointRegistry.getExtensionPoint(ProcessDefinitionContainer.class);
        this.executionInstanceFactory = extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
        this.taskItemInstanceStorage = extensionPointRegistry.getExtensionPoint(TaskItemInstanceStorage.class);

    }

    @Override
    public void stop() {

    }

    @Override
    public void complete(String taskInstanceId, Map<String, Object> variables){
        //获取smart-engine实例
        SmartEngine smartEngine = extensionPointRegistry.getExtensionPoint(SmartEngine.class);
        //获取流程实例配置，包括所有的查询服务和命令及扩展点
        ProcessEngineConfiguration processEngineConfiguration = smartEngine.getProcessEngineConfiguration();
        //持久化工厂扩展点,用于获取各领域的DAO实例
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        //获取子任务存储实例
        TaskItemInstanceStorage taskItemInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskItemInstanceStorage.class);
        //获取主任务下的子任务实例列表
        TaskItemInstanceQueryParam taskItemInstanceQueryParam = new TaskItemInstanceQueryParam();
        taskItemInstanceQueryParam.setTaskInstanceId(taskInstanceId);
        List<TaskItemInstance> taskItemList = taskItemInstanceStorage.findTaskItemList(taskItemInstanceQueryParam,
            processEngineConfiguration);
        if(taskItemList == null || taskItemList.size() <= 0){
            return;
        }
        //获取子任务id列表，传统写法，引擎追求最小依赖，避免各种Jar包冲突
        List<String> subBizIdList = new ArrayList<String>();
        for(TaskItemInstance taskItemInstance : taskItemList){
            subBizIdList.add(taskItemInstance.getSubBizId());
        }
        //批量审批子任务
        complete(taskInstanceId, subBizIdList, variables);
    }


    @Override
    public void complete(String taskInstanceId, String subBizId, Map<String, Object> variables) {
        SmartEngine smartEngine = extensionPointRegistry.getExtensionPoint(SmartEngine.class);
        //获取流程实例配置，包括所有的查询服务和命令及扩展点
        ProcessEngineConfiguration processEngineConfiguration = smartEngine.getProcessEngineConfiguration();
        //持久化工厂扩展点,用于获取各领域的DAO实例
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        //获取子任务存储实例
        TaskItemInstanceStorage taskItemInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskItemInstanceStorage.class);
        //查询子任务实例
        TaskItemInstance taskItemInstance = taskItemInstanceStorage.find(taskInstanceId, subBizId, processEngineConfiguration);

        String processInstanceId = taskItemInstance.getProcessInstanceId();
        String activityInstanceId = taskItemInstance.getActivityInstanceId();
        String processDefinitionActivityId = taskItemInstance.getProcessDefinitionActivityId();
        String processDefinitionIdAndVersion = taskItemInstance.getProcessDefinitionIdAndVersion();
        //获取pvm流程定义
        PvmProcessDefinition pvmProcessDefinition = this.processContainer.getPvmProcessDefinition(processDefinitionIdAndVersion);
        //获取pvm活动节点
        PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(processDefinitionActivityId);
        //获取子任务审批扩展点
        TaskItemCompleteProcessor taskItemCompleteProcessor = processEngineConfiguration.getTaskItemCompleteProcessor();
        //执行前置处理
        taskItemCompleteProcessor.postProcessBeforeTaskItemComplete(processInstanceId, activityInstanceId,
            taskInstanceId, Collections.singletonList(taskItemInstance.getInstanceId()), variables, pvmActivity.getModel(), smartEngine);
        //完成子任务，领域内持久化（不真正入库）
        MarkDoneUtil.markDoneTaskItemInstance(taskItemInstance, TaskInstanceConstant.COMPLETED, TaskInstanceConstant.PENDING,
            variables, taskItemInstanceStorage, processEngineConfiguration);
        //扩展点后置处理
        taskItemCompleteProcessor.postProcessAfterTaskItemComplete(processInstanceId, activityInstanceId,
            taskInstanceId, Collections.singletonList(taskItemInstance.getInstanceId()), variables, pvmActivity.getModel(), smartEngine);
        /*
            根据canCompleteCurrentMainTask返回值决策是否可以关闭当前主任务
                 * 核心处理
                 * 是否可以关闭当前主任务
                 * 返回值： key=canComplete(是否可以关闭当前主任务)
                 * key=tag(如果可以驱动到下一个节点，则当前节点的审核结果的标签值)
                 * 例子：Map<String,String> map = new HashMap<>;
                 * map.put("canComplete","true");
                 * map.put("tag","pass");
         */
        Map<String, String> decision = taskItemCompleteProcessor.canCompleteCurrentMainTask(taskItemInstance.getProcessInstanceId(), taskInstanceId, pvmActivity.getModel(), smartEngine);
        if(decision != null && Boolean.TRUE.toString().equalsIgnoreCase(decision.get("canComplete"))){
            //获取放任务实例
            TaskCommandService taskCommandService = smartEngine.getTaskCommandService();
            //传递上下文参数
            //审批通过时的tag
            variables.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG, decision.get("tag"));
            //行级审批模式
            variables.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_MODE, ProcessInstanceModeConstant.ITEM);
            //活动实例id
            variables.put("activityInstanceId", activityInstanceId);
            //完成主任务，通过子任务驱动主任务实现
            taskCommandService.complete(taskInstanceId, variables);
        }
    }

    @Override
    public void complete(String taskItemId, String subBizId, String userId, Map<String, Object> variables) {
        if(null == variables){
            variables = new HashMap<String, Object>();
        }
        variables.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,userId);
        complete(taskItemId, subBizId, variables);
    }

    @Override
    public void complete(String taskInstanceId, List<String> subBizIds, Map<String, Object> variables) {
        //获取smart-engine实例
        SmartEngine smartEngine = extensionPointRegistry.getExtensionPoint(SmartEngine.class);
        ProcessEngineConfiguration processEngineConfiguration = smartEngine.getProcessEngineConfiguration();
        //持久化工厂扩展点,用于获取各领域的DAO实例
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskItemInstanceStorage taskItemInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskItemInstanceStorage.class);

        //查询子任务列表
        TaskItemInstanceQueryParam taskInstanceQueryParam = new TaskItemInstanceQueryParam();
        taskInstanceQueryParam.setTaskInstanceId(taskInstanceId);
        taskInstanceQueryParam.setSubBizIdList(subBizIds);
        List<TaskItemInstance> taskItemInstanceList = taskItemInstanceStorage.findTaskItemList(taskInstanceQueryParam, processEngineConfiguration);
        if(taskItemInstanceList == null || taskItemInstanceList.size() <= 0){
            return;
        }

        List<String> taskItemIdList = new ArrayList<String>();
        for(TaskItemInstance taskItemInstance : taskItemInstanceList){
            taskItemIdList.add(taskItemInstance.getInstanceId());
        }

        String processInstanceId = taskItemInstanceList.get(0).getProcessInstanceId();
        String activityInstanceId = taskItemInstanceList.get(0).getActivityInstanceId();
        String processDefinitionActivityId = taskItemInstanceList.get(0).getProcessDefinitionActivityId();
        String processDefinitionIdAndVersion = taskItemInstanceList.get(0).getProcessDefinitionIdAndVersion();
        ////获取pvm流程定义
        PvmProcessDefinition pvmProcessDefinition = this.processContainer.getPvmProcessDefinition(processDefinitionIdAndVersion);
        PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(processDefinitionActivityId);
        //获取子任务审批扩展点
        TaskItemCompleteProcessor taskItemCompleteProcessor = processEngineConfiguration.getTaskItemCompleteProcessor();
        //执行前置处理
        taskItemCompleteProcessor.postProcessBeforeTaskItemComplete(processInstanceId, activityInstanceId,
            taskInstanceId, taskItemIdList, variables, pvmActivity.getModel(), smartEngine);
        //完成子任务，领域内持久化（不真正入库）
        MarkDoneUtil.markDoneTaskItemInstance(taskItemInstanceList, TaskInstanceConstant.COMPLETED, TaskInstanceConstant.PENDING,
            variables, taskItemInstanceStorage, processEngineConfiguration);
        //扩展点后置处理
        taskItemCompleteProcessor.postProcessAfterTaskItemComplete(processInstanceId, activityInstanceId,
            taskInstanceId, taskItemIdList, variables, pvmActivity.getModel(), smartEngine);
        /*
        根据canCompleteCurrentMainTask返回值决策是否可以关闭当前主任务
                * 核心处理
                * 是否可以关闭当前主任务
                * 返回值： key=canComplete(是否可以关闭当前主任务)
                * key=tag(如果可以驱动到下一个节点，则当前节点的审核结果的标签值)
                * 例子：Map<String,String> map = new HashMap<>;
                 * map.put("canComplete","true");
                 * map.put("tag","pass");
         */
        Map<String, String> map = taskItemCompleteProcessor.canCompleteCurrentMainTask(processInstanceId, taskInstanceId, pvmActivity.getModel(), smartEngine);
        if(map != null && Boolean.TRUE.toString().equalsIgnoreCase(map.get("canComplete"))){
            TaskCommandService taskCommandService = smartEngine.getTaskCommandService();
            variables.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG, map.get("tag"));
            variables.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_MODE, ProcessInstanceModeConstant.ITEM);
            variables.put("activityInstanceId", activityInstanceId);
            taskCommandService.complete(taskInstanceId, variables);
        }
    }

    @Override
    public void complete(String taskInstanceId, List<String> subBizIds, String userId, Map<String, Object> variables) {
        if(null == variables){
            variables = new HashMap<String, Object>();
        }
        variables.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,userId);
        this.complete(taskInstanceId, subBizIds, variables);
    }

}
