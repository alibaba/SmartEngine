package com.alibaba.smart.framework.engine.service.query.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;

import java.util.List;

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

//        this.executionInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ExecutionInstanceStorage.class);

    }

    @Override
    public void stop() {

    }


    @Override
    public List<ExecutionInstance> findActiveExecutionList(String processInstanceId) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(
            SmartEngine.class).getProcessEngineConfiguration();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        ExecutionInstanceStorage executionInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);

        return executionInstanceStorage.findActiveExecution(processInstanceId, processEngineConfiguration);
    }

    /* add by zhengzheng.hzz */
    @Override
    public List<ExecutionInstance> findByActivityInstanceId(Long processInstanceId, Long activityInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(
            PersisterFactoryExtensionPoint.class);
        ExecutionInstanceStorage executionInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(
            ExecutionInstanceStorage.class);
        return executionInstanceStorage.findByActivityInstanceId(processInstanceId, activityInstanceId);
    }
    /* add by zhengzheng.hzz */
}
