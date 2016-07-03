package com.alibaba.smart.framework.engine.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmProcess;
import com.alibaba.smart.framework.engine.service.ExecutionService;

/**
 * Created by ettear on 16-4-19.
 */
public class DefaultExecutionService implements ExecutionService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
    private ProcessContainer       processContainer;
    private ProcessInstanceStorage processInstanceStorage;
    private InstanceContextFactory instanceContextFactory;

    public DefaultExecutionService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.processContainer = this.extensionPointRegistry.getExtensionPoint(ProcessContainer.class);
        this.processInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
        this.instanceContextFactory = this.extensionPointRegistry.getExtensionPoint(InstanceContextFactory.class);
    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance signal(String processInstanceId, String executionInstanceId, Map<String, Object> variables) {
        ProcessInstance processInstance = this.processInstanceStorage.find(processInstanceId);
        if (null != processInstance) {
            PvmProcess runtimeProcess = this.processContainer.get(processInstance.getProcessUri());
            InstanceContext instanceContext = this.instanceContextFactory.create();
            instanceContext.setProcessInstance(processInstance);
            ExecutionInstance currentExecutionInstance = null;
            for (Map.Entry<String, ExecutionInstance> executionInstanceEntry : processInstance.getExecutions().entrySet()) {
                ExecutionInstance executionInstance = executionInstanceEntry.getValue();
                if (StringUtils.equals(executionInstance.getInstanceId(), executionInstanceId)) {
                    currentExecutionInstance = executionInstance;
                    break;
                }
            }
            instanceContext.setCurrentExecution(currentExecutionInstance);
            runtimeProcess.resume(instanceContext);
        }
        return processInstance;
    }
}
