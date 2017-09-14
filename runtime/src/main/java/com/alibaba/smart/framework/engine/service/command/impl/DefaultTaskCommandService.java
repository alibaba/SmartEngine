package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.service.TaskAssigneeService;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;

import java.util.Date;
import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public class DefaultTaskCommandService implements TaskCommandService , LifeCycleListener {

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
    public void complete(Long taskId, Map<String, Object> variables) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

        TaskInstance taskInstance= taskInstanceStorage.find(taskId);
        Date currentDate = DateUtil.getCurrentDate();
        taskInstance.setCompleteDate(currentDate);
        taskInstance.setEndTime(currentDate);
        taskInstanceStorage.update(taskInstance);



        TaskAssigneeService taskAssigneeService = processEngineConfiguration.getTaskAssigneeService();
        if(null != taskAssigneeService){
            taskAssigneeService.complete(taskInstance.getInstanceId());
        }



        executionCommandService.signal(taskInstance.getExecutionInstanceId(),variables);

    }

    @Override
    public void complete(Long taskId, Long userId, Map<String, Object> variables) {

    }

    @Override
    public void claim(Long taskId, Long userId, Map<String, Object> variables) {

    }
}
