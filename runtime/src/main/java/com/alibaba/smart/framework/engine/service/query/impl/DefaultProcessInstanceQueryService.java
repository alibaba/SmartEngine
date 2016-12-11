package com.alibaba.smart.framework.engine.service.query.impl;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.service.query.ProcessInstanceQueryService;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:10.
 */
public class DefaultProcessInstanceQueryService implements ProcessInstanceQueryService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
//    private ProcessInstanceStorage processInstanceStorage;


    public DefaultProcessInstanceQueryService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {

//        this.processInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);

    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance findOne(Long processInstanceId) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        ProcessInstanceStorage processInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);

        return processInstanceStorage.find(processInstanceId);
    }
}
