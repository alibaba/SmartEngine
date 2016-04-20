package com.alibaba.smart.framework.engine.instance.manager.impl;

import com.alibaba.smart.framework.engine.context.impl.DefaultInstanceContext;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.manager.ProcessManager;
import com.alibaba.smart.framework.engine.instance.store.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcessComponent;

import java.util.Map;

/**
 * DefaultProcessManager
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
        RuntimeProcess runtimeProcess = processComponent.getProcess();
        DefaultInstanceContext instanceContext = new DefaultInstanceContext();
        runtimeProcess.execute(instanceContext);
        return instanceContext.getProcessInstance();
    }

    @Override
    public void abort(String processInstanceId) {

    }

    @Override
    public ProcessInstance find(String processInstanceId) {
        return this.processInstanceStorage.load(processInstanceId);
    }
}
