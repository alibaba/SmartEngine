package com.alibaba.smart.framework.engine.service.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.ProcessService;

/**
 * DefaultProcessManager Created by ettear on 16-4-19.
 */
public class DefaultProcessService implements ProcessService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
    private ProcessDefinitionContainer       processDefinitionContainer;
    private ProcessInstanceStorage processInstanceStorage;
    private InstanceContextFactory instanceContextFactory;
    private ProcessInstanceFactory processInstanceFactory;
    ExecutionInstanceFactory       executionInstanceFactory;

    // private InstanceFactFactory factFactory;

    public DefaultProcessService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.processDefinitionContainer = this.extensionPointRegistry.getExtensionPoint(ProcessDefinitionContainer.class);
        this.processInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
        this.instanceContextFactory = this.extensionPointRegistry.getExtensionPoint(InstanceContextFactory.class);
        this.processInstanceFactory = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceFactory.class);
        this.executionInstanceFactory = this.extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
        // this.factFactory = this.extensionPointRegistry.getExtensionPoint(InstanceFactFactory.class);

    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance start(String processId, String version, Map<String, Object> request) {
        PvmProcessDefinition pvmProcessDefinition = this.processDefinitionContainer.get(processId, version);
        
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();
        
        ProcessInstance processInstance = this.processInstanceFactory.create();

        ExecutionInstance executionInstance = this.executionInstanceFactory.create();
        executionInstance.setProcessInstanceId(processInstance.getInstanceId());
        // executionInstance.setFact(factFactory.create(variables));

        processInstance.setProcessUri(pvmProcessDefinition.getUri());
        processInstance.addExecution(executionInstance);// 执行实例添加到流程实例

        ExecutionContext executionContext = this.instanceContextFactory.create();
        executionContext.setProcessInstance(processInstance);
        executionContext.setCurrentExecution(executionInstance);// 执行实例添加到当前上下文中
        executionContext.setPvmProcessDefinition(pvmProcessDefinition);
        executionContext.setRequest(request);

        pvmProcessInstance.start(executionContext);
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
