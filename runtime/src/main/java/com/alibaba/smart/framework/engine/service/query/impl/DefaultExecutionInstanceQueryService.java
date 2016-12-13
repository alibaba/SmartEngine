package com.alibaba.smart.framework.engine.service.query.impl;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.query.ExecutionInstanceQueryService;

import java.util.List;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:10.
 */
public class DefaultExecutionInstanceQueryService implements ExecutionInstanceQueryService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
    private ExecutionInstanceStorage executionInstanceStorage;


    public DefaultExecutionInstanceQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {

//        this.executionInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ExecutionInstanceStorage.class);

    }

    @Override
    public void stop() {

    }


    @Override
    public List<ExecutionInstance> findActiveExecution(Long processInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        ExecutionInstanceStorage executionInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);

        return executionInstanceStorage.findActiveExecution(processInstanceId);
    }
}
