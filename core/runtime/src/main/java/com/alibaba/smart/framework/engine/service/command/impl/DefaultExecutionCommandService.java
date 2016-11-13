package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public class DefaultExecutionCommandService implements ExecutionCommandService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
    private ProcessDefinitionContainer       processContainer;
    private InstanceContextFactory instanceContextFactory;

    private ProcessInstanceStorage processInstanceStorage;
    private ActivityInstanceStorage activityInstanceStorage;
    private ExecutionInstanceStorage executionInstanceStorage;


    public DefaultExecutionCommandService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.processContainer = this.extensionPointRegistry.getExtensionPoint(ProcessDefinitionContainer.class);
        this.instanceContextFactory = this.extensionPointRegistry.getExtensionPoint(InstanceContextFactory.class);

        this.processInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
        this.activityInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ActivityInstanceStorage.class);
        this.executionInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ExecutionInstanceStorage.class);

    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance signal( String executionInstanceId, Map<String, Object> request) {
        ExecutionInstance executionInstance =  this.executionInstanceStorage.find(executionInstanceId);
        ProcessInstance processInstance = this.processInstanceStorage.find(executionInstance.getProcessInstanceId());

        PvmProcessDefinition pvmProcessDefinition = this.processContainer.get(processInstance.getProcessUri());
        String activityId= executionInstance.getActivityId();
        PvmActivity pvmActivity= pvmProcessDefinition.getActivities().get(activityId);


        ExecutionContext executionContext = this.instanceContextFactory.create();
        executionContext.setExtensionPointRegistry(this.extensionPointRegistry);
        executionContext.setPvmProcessDefinition(pvmProcessDefinition);
        executionContext.setRequest(request);

        //TODO TUNE 减少不必要的对象创建
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();
        ProcessInstance newProcessInstance =  pvmProcessInstance.signal( pvmActivity,executionContext);

        return newProcessInstance;
    }
}
