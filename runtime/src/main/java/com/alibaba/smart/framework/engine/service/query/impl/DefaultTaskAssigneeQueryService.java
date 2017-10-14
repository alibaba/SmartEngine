package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.VariablePersister;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.query.TaskAssigneeQueryService;
import com.alibaba.smart.framework.engine.service.query.VariableQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2017 October  07:46.
 */
public class DefaultTaskAssigneeQueryService implements TaskAssigneeQueryService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;

    public DefaultTaskAssigneeQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public List<TaskAssigneeInstance> findList(Long taskInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskAssigneeStorage taskAssigneeStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskAssigneeStorage.class);
        List<TaskAssigneeInstance>  taskAssigneeStorageList =  taskAssigneeStorage.findList(taskInstanceId);
        return taskAssigneeStorageList;
    }
}

