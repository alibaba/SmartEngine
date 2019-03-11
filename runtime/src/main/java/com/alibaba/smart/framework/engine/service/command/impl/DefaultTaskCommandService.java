package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.exception.ConcurrentException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskItemInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public class DefaultTaskCommandService implements TaskCommandService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;

    private ProcessInstanceStorage processInstanceStorage;
    private ActivityInstanceStorage activityInstanceStorage;
    private ExecutionInstanceStorage executionInstanceStorage;
    private ExecutionCommandService executionCommandService;


    public DefaultTaskCommandService(ExtensionPointRegistry extensionPointRegistry) {
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
    public void complete(String taskId, Map<String, Object> variables) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(
            SmartEngine.class).getProcessEngineConfiguration();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(
            PersisterFactoryExtensionPoint.class);

        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(
            TaskInstanceStorage.class);
        TaskInstance taskInstance = taskInstanceStorage.find(taskId,processEngineConfiguration );
        MarkDoneUtil.markDoneTaskInstance(taskInstance, TaskInstanceConstant.COMPLETED, TaskInstanceConstant.PENDING,
            variables, taskInstanceStorage, processEngineConfiguration);

        executionCommandService.signal(taskInstance.getExecutionInstanceId(), variables);

    }



    @Override
    public void complete(String taskId, String userId, Map<String, Object> variables) {
        if(null == variables){
            variables = new HashMap<String, Object>();
        }
        variables.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,userId);

        //TODO check priviage

        complete(  taskId, variables);
    }

    @Override
    public void cancel(String taskId, Map<String, Object> variables) {
        if(variables == null){
            variables = new HashMap<String, Object>();
        }
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(
            SmartEngine.class).getProcessEngineConfiguration();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(
            PersisterFactoryExtensionPoint.class);

        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);
        ExecutionInstanceStorage executionInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);

        TaskInstance taskInstance = taskInstanceStorage.find(taskId,processEngineConfiguration);
        if(TaskInstanceConstant.PENDING.equals(taskInstance.getStatus())) {
            MarkDoneUtil.markDoneTaskInstance(taskInstance, TaskInstanceConstant.CANCELED, TaskInstanceConstant.PENDING,
                variables, taskInstanceStorage, processEngineConfiguration);

            String executionInstanceId = taskInstance.getExecutionInstanceId();
            ExecutionInstance executionInstance = executionInstanceStorage.find(executionInstanceId, processEngineConfiguration);
            MarkDoneUtil.markDoneExecutionInstance(executionInstance, executionInstanceStorage, processEngineConfiguration);

            TaskItemInstanceStorage taskItemInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskItemInstanceStorage.class);
            TaskItemInstanceQueryParam taskItemInstanceQueryParam = new TaskItemInstanceQueryParam();
            taskItemInstanceQueryParam.setTaskInstanceId(taskId);
            taskItemInstanceQueryParam.setStatus(TaskInstanceConstant.PENDING);
            List<TaskItemInstance> taskItemList = taskItemInstanceStorage.findTaskItemList(taskItemInstanceQueryParam,processEngineConfiguration);
            if(taskItemList != null && taskItemList.size() > 0){
                MarkDoneUtil.markDoneTaskItemInstance(taskItemList, TaskInstanceConstant.CANCELED,
                    TaskInstanceConstant.PENDING,variables, taskItemInstanceStorage, processEngineConfiguration);
            }
        }
    }

    //@Override
    //public void claim(Long taskId, String userId, Map<String, Object> variables) {
    //
    //}
}
