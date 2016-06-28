package com.alibaba.smart.framework.engine.instance.manager.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.InstanceFactFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.manager.ProcessManager;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;

/**
 * DefaultProcessManager Created by ettear on 16-4-19.
 */
public class DefaultProcessManager implements ProcessManager, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
    private ProcessContainer       processContainer;
    private ProcessInstanceStorage processInstanceStorage;
    private InstanceContextFactory instanceContextFactory;
    private ProcessInstanceFactory processInstanceFactory;
    ExecutionInstanceFactory       executionInstanceFactory;
    private InstanceFactFactory    factFactory;

    public DefaultProcessManager(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.processContainer = this.extensionPointRegistry.getExtensionPoint(ProcessContainer.class);
        this.processInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
        this.instanceContextFactory = this.extensionPointRegistry.getExtensionPoint(InstanceContextFactory.class);
        this.processInstanceFactory = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceFactory.class);
        this.executionInstanceFactory = this.extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
        this.factFactory = this.extensionPointRegistry.getExtensionPoint(InstanceFactFactory.class);

    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance start(String processId, String version, Map<String, Object> variables) {
        RuntimeProcess runtimeProcess = this.processContainer.get(processId, version);
        ProcessInstance processInstance = this.processInstanceFactory.create();

        ExecutionInstance executionInstance = this.executionInstanceFactory.create();
        executionInstance.setProcessInstanceId(processInstance.getInstanceId());
        executionInstance.setFact(factFactory.create(variables));

        processInstance.setProcessUri(runtimeProcess.getUri());
        processInstance.addExecution(executionInstance);// 执行实例添加到流程实例

        InstanceContext instanceContext = this.instanceContextFactory.create();
        instanceContext.setProcessInstance(processInstance);
        instanceContext.setCurrentExecution(executionInstance);// 执行实例添加到当前上下文中

        runtimeProcess.run(instanceContext);
        return processInstance;
    }

    @Override
    public void abort(String processInstanceId) {

    }

    @Override
    public ProcessInstance find(String processInstanceId) {
        return this.processInstanceStorage.find(processInstanceId);
    }
}
