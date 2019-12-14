package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.query.TaskAssigneeQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  07:46.
 */
public class DefaultTaskAssigneeQueryService implements TaskAssigneeQueryService, LifeCycleHook {

    private final ProcessEngineConfiguration processEngineConfiguration;
    private ExtensionPointRegistry extensionPointRegistry;

    public DefaultTaskAssigneeQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
        this.processEngineConfiguration = extensionPointRegistry.getExtensionPoint(
            SmartEngine.class).getProcessEngineConfiguration();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public List<TaskAssigneeInstance> findList(String taskInstanceId) {


        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskAssigneeStorage taskAssigneeStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskAssigneeStorage.class);
        List<TaskAssigneeInstance>  taskAssigneeStorageList =  taskAssigneeStorage.findList(taskInstanceId, processEngineConfiguration);
        return taskAssigneeStorageList;
    }

    @Override
    public Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskAssigneeStorage taskAssigneeStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskAssigneeStorage.class);
        return taskAssigneeStorage.findAssigneeOfInstanceList(taskInstanceIdList,processEngineConfiguration );
    }
}

