package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:10.
 */
public class DefaultExecutionQueryService implements ExecutionQueryService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
    private ExecutionInstanceStorage executionInstanceStorage;

    public DefaultExecutionQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {

        //        this.executionInstanceStorage = this.extensionPointRegistry.getExtensionPoint
        // (ExecutionInstanceStorage.class);

    }

    @Override
    public void stop() {

    }

    @Override
    public List<ExecutionInstance> findActiveExecutionList(Long processInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(
            PersisterFactoryExtensionPoint.class);
        ExecutionInstanceStorage executionInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(
            ExecutionInstanceStorage.class);

        return executionInstanceStorage.findActiveExecution(processInstanceId);
    }

    @Override
    public List<ExecutionInstance> findByActivityInstanceId(Long processInstanceId, Long activityInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(
            PersisterFactoryExtensionPoint.class);
        ExecutionInstanceStorage executionInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(
            ExecutionInstanceStorage.class);
        return executionInstanceStorage.findByActivityInstanceId(processInstanceId, activityInstanceId);
    }
}
