package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public class DefaultTaskCommandService implements TaskCommandService, LifeCycleHook {

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
    public void complete(String taskId, Map<String, Object> request, Map<String, Object> response) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(
            SmartEngine.class).getProcessEngineConfiguration();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(
            PersisterFactoryExtensionPoint.class);

        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(
            TaskInstanceStorage.class);
        TaskInstance taskInstance = taskInstanceStorage.find(taskId,processEngineConfiguration );
        MarkDoneUtil.markDoneTaskInstance(taskInstance, TaskInstanceConstant.COMPLETED, TaskInstanceConstant.PENDING,
            request, taskInstanceStorage, processEngineConfiguration);

        executionCommandService.signal(taskInstance.getExecutionInstanceId(), request,response);

    }

    @Override
    public void complete(String taskId, Map<String, Object> request) {
        this.complete(taskId,request,null);
    }

    @Override
    public void complete(String taskId, String userId, Map<String, Object> request) {
        if(null == request){
            request = new HashMap<String, Object>();
        }
        request.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_CLAIM_USER_ID,userId);

        //TODO check priviage

        complete(  taskId, request);
    }



    //@Override
    //public void claim(Long taskId, String userId, Map<String, Object> variables) {
    //
    //}
}
