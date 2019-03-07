package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.ProcessInstanceModeConstant;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskItemInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskItemCommandService;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryParam;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public class DefaultTaskItemCommandService implements TaskItemCommandService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;

    private ProcessInstanceStorage processInstanceStorage;
    private ActivityInstanceStorage activityInstanceStorage;
    private ExecutionInstanceStorage executionInstanceStorage;
    private ExecutionCommandService executionCommandService;


    public DefaultTaskItemCommandService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.executionCommandService = this.extensionPointRegistry.getExtensionPoint(ExecutionCommandService.class);
        this.processInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
        this.activityInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ActivityInstanceStorage.class);
        this.executionInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ExecutionInstanceStorage.class);

    }

    @Override
    public void stop() {

    }


    @Override
    public void complete(String taskInstanceId, String subBizId, Map<String, Object> variables) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);
        TaskItemInstanceStorage taskItemInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskItemInstanceStorage.class);

        TaskItemInstance taskItemInstance = taskItemInstanceStorage.find(taskInstanceId, subBizId, processEngineConfiguration);
        TaskInstance taskInstance = taskInstanceStorage.find(String.valueOf(taskInstanceId), processEngineConfiguration);

        variables.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_MODE, ProcessInstanceModeConstant.ITEM);
        executionCommandService.signal(taskItemInstance.getExecutionInstanceId(), variables);

        MarkDoneUtil.markDoneTaskItemInstance(taskItemInstance, TaskInstanceConstant.COMPLETED, TaskInstanceConstant.PENDING, variables, taskItemInstanceStorage, processEngineConfiguration);
        executionCommandService.signal(taskInstance.getExecutionInstanceId(), variables);
    }



    @Override
    public void complete(String taskItemId, String subBizId, String userId, Map<String, Object> variables) {
        if(null == variables){
            variables = new HashMap<String, Object>();
        }
        variables.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,userId);

        //TODO check priviage

        complete(taskItemId, subBizId, variables);
    }

    @Override
    public void complete(String taskInstanceId, List<String> subBizIds, Map<String, Object> variables) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);
        TaskItemInstanceStorage taskItemInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskItemInstanceStorage.class);

        //查询子任务列表
        TaskItemInstanceQueryParam taskInstanceQueryParam = new TaskItemInstanceQueryParam();
        taskInstanceQueryParam.setTaskInstanceId(taskInstanceId);
        taskInstanceQueryParam.setSubBizIdList(subBizIds);
        List<TaskItemInstance> taskItemInstanceList = taskItemInstanceStorage.findTaskList(taskInstanceQueryParam, processEngineConfiguration);
        variables.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_MODE, ProcessInstanceModeConstant.ITEM);

        MarkDoneUtil.markDoneTaskItemInstance(taskItemInstanceList, TaskInstanceConstant.COMPLETED, TaskInstanceConstant.PENDING, variables, taskItemInstanceStorage, processEngineConfiguration);
        //TODO 驱动逻辑==>子任务驱动主任务
        executionCommandService.signal(taskItemInstanceList.get(0).getExecutionInstanceId(), variables);
    }

    @Override
    public void complete(String taskInstanceId, List<String> subBizIds, String userId, Map<String, Object> variables) {
        if(null == variables){
            variables = new HashMap<String, Object>();
        }
        variables.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,userId);
        this.complete(taskInstanceId,subBizIds,variables);
    }

    //@Override
    //public void claim(Long taskId, String userId, Map<String, Object> variables) {
    //
    //}
}
