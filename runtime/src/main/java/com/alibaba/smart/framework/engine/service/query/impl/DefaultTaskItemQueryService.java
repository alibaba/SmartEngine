package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.TaskItemInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskItemInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.param.query.TaskItemInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.TaskItemQueryService;

public class DefaultTaskItemQueryService implements TaskItemQueryService {

    private ExtensionPointRegistry extensionPointRegistry;
    private ProcessEngineConfiguration processEngineConfiguration ;

    public DefaultTaskItemQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
        this.processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
    }
    @Override
    public Long count(TaskItemInstanceQueryParam taskItemInstanceQueryParam) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskItemInstanceStorage taskItemInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskItemInstanceStorage.class);
        return taskItemInstanceStorage.count(taskItemInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public List<TaskItemInstance> findTaskItemList(TaskItemInstanceQueryParam taskItemInstanceQueryParam) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        TaskItemInstanceStorage taskItemInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskItemInstanceStorage.class);
        return taskItemInstanceStorage.findTaskItemList(taskItemInstanceQueryParam, processEngineConfiguration);
    }
}
