package com.alibaba.smart.framework.engine.instance.manager.impl;

import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.manager.ProcessInstanceManager;
import com.alibaba.smart.framework.engine.instance.store.ProcessInstanceStorage;

import java.util.UUID;

/**
 * Created by ettear on 16-4-19.
 */
public class DefaultProcessInstanceManager implements ProcessInstanceManager, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
    private ProcessInstanceStorage processInstanceStorage;

    public DefaultProcessInstanceManager(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.processInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance create(ProcessInstance processInstance) {
        processInstance.setInstanceId(UUID.randomUUID().toString());
        this.processInstanceStorage.save(processInstance);
        return processInstance;
    }

    @Override
    public void abort(String instanceId) {

    }

    @Override
    public ProcessInstance load(String instanceId) {
        return this.processInstanceStorage.load(instanceId);
    }
}
