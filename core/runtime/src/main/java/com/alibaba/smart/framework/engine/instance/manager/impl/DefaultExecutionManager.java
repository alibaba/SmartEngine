package com.alibaba.smart.framework.engine.instance.manager.impl;

import com.alibaba.smart.framework.engine.context.Fact;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.context.factory.FactFactory;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.context.storage.FactStorage;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.manager.ExecutionManager;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by ettear on 16-4-19.
 */
public class DefaultExecutionManager implements ExecutionManager, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
    private ProcessContainer       processContainer;
    private ProcessInstanceStorage processInstanceStorage;
    private FactStorage            factStorage;
    private InstanceContextFactory instanceContextFactory;
    private FactFactory            factFactory;

    public DefaultExecutionManager(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.processContainer = this.extensionPointRegistry.getExtensionPoint(ProcessContainer.class);
        this.processInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
        this.factStorage=this.extensionPointRegistry.getExtensionPoint(FactStorage.class);
        this.instanceContextFactory = this.extensionPointRegistry.getExtensionPoint(InstanceContextFactory.class);
        this.factFactory=this.extensionPointRegistry.getExtensionPoint(FactFactory.class);
    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance signal(String processInstanceId, String executionInstanceId, Map<String, Object> variables) {
        ProcessInstance processInstance = this.processInstanceStorage.find(processInstanceId);
        if (null != processInstance) {
            RuntimeProcess runtimeProcess = this.processContainer.get(processInstance.getProcessUri());
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

            Fact processFact=this.factStorage.findProcessFact(processInstanceId);
            if(null==processFact){
                processFact=this.factFactory.create();
            }
            instanceContext.setProcessFact(processFact);

            Fact executionFact=this.factStorage.findActivityFact(processInstanceId,currentExecutionInstance.getInstanceId());
            if(null==executionFact){
                executionFact=this.factFactory.create();
            }
            if(null!=variables && !variables.isEmpty()){
                executionFact.putAll(variables);
            }
            instanceContext.setExecutionFact(executionFact);

            runtimeProcess.resume(instanceContext);
        }
        return processInstance;
    }
}
