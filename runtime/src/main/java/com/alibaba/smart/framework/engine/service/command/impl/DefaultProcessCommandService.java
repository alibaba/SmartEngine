package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extensionpoint.impl.DefaultPersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;

import java.util.List;
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
        PvmProcessDefinition pvmProcessDefinition = this.processDefinitionContainer.get(processDefinitionId, version);

        ExecutionContext executionContext = this.instanceContextFactory.create();
        executionContext.setExtensionPointRegistry(this.extensionPointRegistry);
        executionContext.setPvmProcessDefinition(pvmProcessDefinition);
        executionContext.setRequest(request);

        //TODO TUNE 减少不必要的对象创建
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();
        ProcessInstance processInstance = pvmProcessInstance.start(executionContext);

        persist(processInstance);

        return processInstance;

    }

    public ProcessInstance start(String processDefinitionId, String version){
        return this.start(processDefinitionId,version,null);
    }

    private void persist(ProcessInstance processInstance) {

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        ProcessInstanceStorage processInstanceStorage =persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);
        ActivityInstanceStorage activityInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ActivityInstanceStorage.class);
        ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);

        processInstanceStorage.save(processInstance);
        List<ActivityInstance> activityInstances = processInstance.getNewActivityInstances();
        for (ActivityInstance activityInstance : activityInstances) {
            activityInstanceStorage.save(activityInstance);

            ExecutionInstance executionInstance = activityInstance.getExecutionInstance();
            if (null != executionInstance) {
                executionInstanceStorage.save(executionInstance);
            }
        }
    }

    @Override
    public void abort(Long processInstanceId) {
        this.abort(processInstanceId,null);
    }

    @Override
    public void abort(Long processInstanceId,String reason){

    }


//    @Override
//    public ProcessInstance find(String processInstanceId) {
//        return this.processInstanceStorage.find(processInstanceId);
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
//            throw new EngineException("can not find process definition");
//        }
//
//        if (null != processInstanceStorage.find(processInstance.getInstanceId())) {
//            processInstance = processInstanceStorage.find(processInstance.getInstanceId());
//        }
//        executionInstance.setActivityId(activityInstance.getActivityId());
//        processInstance.setProcessUri(processDefinition.getUri());
////        processInstance.addExecution(executionInstance);
//        processInstanceStorage.save(processInstance);
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
//            throw new EngineException("not find activity,check process definition");
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
//        ProcessInstance processInstance = processInstanceStorage.find(processId);
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
