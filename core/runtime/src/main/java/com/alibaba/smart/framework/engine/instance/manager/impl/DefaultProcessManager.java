package com.alibaba.smart.framework.engine.instance.manager.impl;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.instance.manager.ProcessManager;
import com.alibaba.smart.framework.engine.instance.store.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcessComponent;

import java.util.Map;

/**
 * Created by ettear on 16-4-19.
 */
public class DefaultProcessManager implements ProcessManager, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
    private ProcessContainer       processContainer;
    private ProcessInstanceStorage processInstanceStorage;

    public DefaultProcessManager(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.processContainer = this.extensionPointRegistry.getExtensionPoint(ProcessContainer.class);
        this.processInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance start(String processId, String version, Map<String, Object> variables) {
        RuntimeProcessComponent processComponent = this.processContainer.get(processId, version);
        ProcessInstance processInstance = this.createInstance(processComponent);
        InstanceContext instanceContext = null;
        //TODO ettear
        this.processInstanceStorage.save(processInstance);
        return processInstance;
    }

    @Override
    public void abort(String processInstanceId) {

    }

    @Override
    public ProcessInstance find(String processInstanceId) {
        return this.processInstanceStorage.load(processInstanceId);
    }

    private ProcessInstance createInstance(RuntimeProcessComponent processComponent) {
        DefaultProcessInstance instance = new DefaultProcessInstance();
        //TODO ettear
        return instance;
    }
}
