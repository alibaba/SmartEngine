package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.HashMap;
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

        MarkDoneUtil.markDoneTaskInstance(taskInstance, TaskInstanceConstant.COMPLETED, TaskInstanceConstant.PENDING, variables, taskInstanceStorage, processEngineConfiguration);
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

    //@Override
    //public void claim(Long taskId, String userId, Map<String, Object> variables) {
    //
    //}
}
