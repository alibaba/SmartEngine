package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;

import java.util.Map;


/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public class DefaultProcessCommandService implements ProcessCommandService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;

    private ProcessDefinitionContainer processDefinitionContainer;

    private InstanceContextFactory instanceContextFactory;
    private ProcessInstanceFactory processInstanceFactory;
    private ExecutionInstanceFactory executionInstanceFactory;
    private ActivityInstanceFactory activityInstanceFactory;



    public DefaultProcessCommandService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.processDefinitionContainer = this.extensionPointRegistry.getExtensionPoint(ProcessDefinitionContainer.class);
        this.instanceContextFactory = this.extensionPointRegistry.getExtensionPoint(InstanceContextFactory.class);
        this.processInstanceFactory = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceFactory.class);
        this.executionInstanceFactory = this.extensionPointRegistry.getExtensionPoint(ExecutionInstanceFactory.class);
        this.activityInstanceFactory = this.extensionPointRegistry.getExtensionPoint(ActivityInstanceFactory.class);

    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance start(String processDefinitionId, String version, Map<String, Object> request) {

        ExecutionContext executionContext = this.instanceContextFactory.create();
        executionContext.setExtensionPointRegistry(this.extensionPointRegistry);
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
        executionContext.setProcessEngineConfiguration(processEngineConfiguration);
        executionContext.setRequest(request);


        PvmProcessDefinition pvmProcessDefinition = this.processDefinitionContainer.get(processDefinitionId, version);
        executionContext.setPvmProcessDefinition(pvmProcessDefinition);

        //TODO TUNE 减少不必要的对象创建
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

        ProcessInstance processInstance = processInstanceFactory.create(executionContext);


        executionContext.setProcessInstance(processInstance);

        processInstance = pvmProcessInstance.start(executionContext);

        processInstance = CommonServiceHelper.insertAndPersist(processInstance, request,  extensionPointRegistry);

        return processInstance;

    }

    @Override
    public ProcessInstance start(String processDefinitionId, String version){
        return this.start(processDefinitionId,version,null);
    }





    @Override
    public void abort(Long processInstanceId) {
        this.abort(processInstanceId,null);
    }

    @Override
    public void abort(Long processInstanceId,String reason){

    }


//    @Override
//    public ProcessInstance findAll(String processInstanceId) {
//        return this.processInstanceStorage.findAll(processInstanceId);
//    }

//    @Override
//    public void recovery(EngineParam engineParam) {
//
//
//        ProcessInstance processInstance = this.processInstanceFactory.recovery(engineParam.getProcessParam());
//        ExecutionInstance executionInstance = this.executionInstanceFactory.recovery(engineParam.getExecutionParam());
//        ActivityInstance activityInstance = this.activityInstanceFactory.recovery(engineParam.getActivityParam());
//
//
//        if (!processInstance.getInstanceId().equals(executionInstance.getProcessInstanceId())
//                || !executionInstance.getProcessInstanceId().equals(activityInstance.getProcessInstanceId())) {
//
//            throw new EngineException("recovery instance is not right!");
//
//        }
//        PvmProcessDefinition processDefinition = null;
//
//        if (null == processDefinition) {
//            throw new EngineException("can not findAll process definition");
//        }
//
//        if (null != processInstanceStorage.findAll(processInstance.getInstanceId())) {
//            processInstance = processInstanceStorage.findAll(processInstance.getInstanceId());
//        }
//        executionInstance.setActivityId(activityInstance.getActivityId());
//        processInstance.setProcessUri(processDefinition.getUri());
////        processInstance.addExecution(executionInstance);
//        processInstanceStorage.insert(processInstance);
//
//
//    }

//    @Override
//    public ProcessInstance run(ProcessDefinition definition,String instanceId, String activityId, boolean sub,Map<String,Object> request) {
//
//
//        ProcessInstance processInstance = getProcessInstance(instanceId,sub);
//        PvmProcessInstance pvmProcess = new DefaultPvmProcessInstance();
//
//        ExecutionInstance chosenExecution  = null;
//        for (ExecutionInstance executionInstance : processInstance.getExecutions().values()) {
//            if (StringUtils.equalsIgnoreCase(executionInstance.getActivity().getActivityId(),activityId)) {
//                chosenExecution = executionInstance;
//                break;
//            }
//
//        }
//        if (chosenExecution == null) {
//            throw new EngineException("not findAll activity,check process definition");
//        }
//        ExecutionContext instanceContext = this.instanceContextFactory.create();
//        instanceContext.setProcessInstance(processInstance);
//        instanceContext.setCurrentExecution(chosenExecution);// 执行实例添加到当前上下文中
//        instanceContext.setRequest(request);
//        PvmProcessDefinition pvmProcessDefinition = this.processDefinitionContainer.get(definition.getId(), definition.getVersion());
//        instanceContext.setPvmProcessDefinition(pvmProcessDefinition);
//
//        pvmProcess.run(instanceContext);
//        return processInstance;
//
//    }


//    private ProcessInstance getProcessInstance(String processId, boolean sub) {
//        ProcessInstance processInstance = processInstanceStorage.findAll(processId);
//
//        return processInstance;
//
//    }
//
//    @Override
//    public void clear(String processId) {
//        processInstanceStorage.remove(processId);
//    }


}
