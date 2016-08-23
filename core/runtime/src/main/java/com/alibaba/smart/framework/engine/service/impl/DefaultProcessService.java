package com.alibaba.smart.framework.engine.service.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.param.EngineParam;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.result.EngineResult;
import com.alibaba.smart.framework.engine.service.ProcessService;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;



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
    private ActivityInstanceFactory activityInstanceFactory;

//    private static String DEFAULT_VERSION= "1";

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
        this.activityInstanceFactory = this.extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);
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
    public ProcessInstance start(String processId) {
        return this.start(processId,ProcessDefinition.DEFAULT_VERSION,null);
    }

    @Override
    public void abort(String processInstanceId) {

    }

    @Override
    public ProcessInstance find(String processInstanceId) {
        return this.processInstanceStorage.find(processInstanceId);
    }

    @Override
    public void recovery(EngineParam engineParam) {


        ProcessInstance processInstance = this.processInstanceFactory.recovery(engineParam.getProcessParam());
        ExecutionInstance executionInstance = this.executionInstanceFactory.recovery(engineParam.getExecutionParam());
        ActivityInstance activityInstance = this.activityInstanceFactory.recovery(engineParam.getActivityParam());


        if (!processInstance.getInstanceId().equals(executionInstance.getProcessInstanceId())
                || !executionInstance.getProcessInstanceId().equals(activityInstance.getProcessInstanceId())) {

            throw new EngineException("recovery instance is not right!");

        }
        PvmProcessDefinition processDefinition = this.processDefinitionContainer.get(
                engineParam.getProcessParam().getProcessDefationId(),
                engineParam.getProcessParam().getProcessDefationVersion());

        if (null == processDefinition) {
            throw new EngineException("can not find process defiation");
        }

        if (null != processInstanceStorage.find(processInstance.getInstanceId())) {
            processInstance = processInstanceStorage.find(processInstance.getInstanceId());
        }
        executionInstance.setActivity(activityInstance);
        processInstance.setProcessUri(processDefinition.getUri());
        processInstance.addExecution(executionInstance);
        processInstanceStorage.save(processInstance);


    }

    @Override
    public ProcessInstance run(ProcessDefinition definition,String processId, String activityId, boolean sub) {


        ProcessInstance processInstance = getProcessInstance(processId,sub);
        PvmProcessInstance pvmProcess = new DefaultPvmProcessInstance();

        ExecutionInstance chosenExecution  = null;
        for (ExecutionInstance executionInstance : processInstance.getExecutions().values()) {
            if (StringUtils.equalsIgnoreCase(executionInstance.getActivity().getActivityId(),activityId)) {
                chosenExecution = executionInstance;
                break;
            }

        }
        if (chosenExecution == null) {
            throw new EngineException("not find activiy,check process defintion");
        }
        ExecutionContext instanceContext = this.instanceContextFactory.create();
        instanceContext.setProcessInstance(processInstance);
        instanceContext.setCurrentExecution(chosenExecution);// 执行实例添加到当前上下文中

        pvmProcess.start(instanceContext);
        return processInstance;

    }

    private ProcessInstance getProcessInstance(String processId, boolean sub) {
        ProcessInstance processInstance ;
        if (!sub) {
            processInstance = processInstanceStorage.find(processId);
            if (null == processInstance) {
                throw new EngineException("process instance is null");
            }

        }else {
            processInstance = processInstanceStorage.findSubProcess(processId);
            if (null == processInstance) {
                throw new EngineException("sub process instance is null");
            }

        }
        return processInstance;

    }

    @Override
    public void clear(String processId) {
        processInstanceStorage.remove(processId);
    }

    @Override
    public EngineResult toDatabase(String processId) {
        ProcessInstance processInstance = this.processInstanceStorage.find(processId);
        if (processInstance == null) {
            throw new EngineException("can not find process instance");
        }

        EngineResult result = new EngineResult();

        StringBuilder exResult = new StringBuilder();
        for (ExecutionInstance executionInstance: processInstance.getExecutions().values()) {
            exResult.append(this.executionInstanceFactory.toDatabase(executionInstance));
            exResult.append(";");
        }

        result.setExecutionsData(exResult.toString());
        return result;


    }



}
