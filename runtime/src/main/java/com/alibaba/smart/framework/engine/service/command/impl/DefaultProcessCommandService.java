package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.DeploymentQueryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public class DefaultProcessCommandService implements ProcessCommandService, LifeCycleHook {

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
    public ProcessInstance start(String processDefinitionId, String processDefinitionVersion,
                                 Map<String, Object> request) {
        return this.start(processDefinitionId,processDefinitionVersion,request,null);
    }

    @Override
    public ProcessInstance start(String processDefinitionId, String processDefinitionVersion, Map<String, Object> request, Map<String, Object> response) {

        ExecutionContext executionContext = this.instanceContextFactory.create();
        executionContext.setExtensionPointRegistry(this.extensionPointRegistry);
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
        executionContext.setProcessEngineConfiguration(processEngineConfiguration);
        executionContext.setRequest(request);
        executionContext.setResponse(response);


        ProcessDefinition processDefinition = this.processDefinitionContainer.getProcessDefinition(processDefinitionId,
            processDefinitionVersion);

        if(null == processDefinition){
            throw new EngineException("No ProcessDefinition found for processDefinitionId : "+processDefinitionId+",processDefinitionVersion : " +processDefinitionVersion);
        }

        executionContext.setProcessDefinition(processDefinition);

        // TUNE 减少不必要的对象创建
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

        ProcessInstance processInstance = processInstanceFactory.create(executionContext);
        try {
            //!!! 重要
            tryInsertProcessInstanceAndLock(processEngineConfiguration, processInstance);

            executionContext.setProcessInstance(processInstance);

            processInstance = pvmProcessInstance.start(executionContext);

            processInstance = CommonServiceHelper.insertAndPersist(processInstance, request, extensionPointRegistry);

            return processInstance;
        } finally {
            LockStrategy lockStrategy = processEngineConfiguration.getLockStrategy();
            if (null != lockStrategy) {
                lockStrategy.unLock(processInstance.getInstanceId());
            }
        }
    }

    private void tryInsertProcessInstanceAndLock(ProcessEngineConfiguration processEngineConfiguration,
                                                 ProcessInstance processInstance) {
        LockStrategy lockStrategy = processEngineConfiguration.getLockStrategy();
        if(null != lockStrategy){

            PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

            ProcessInstanceStorage processInstanceStorage =persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);

            ProcessInstance newProcessInstance =  processInstanceStorage.insert(processInstance, processEngineConfiguration);

            lockStrategy.tryLock(newProcessInstance.getInstanceId());
        }
    }

    @Override
    public ProcessInstance start(String processDefinitionId, String processDefinitionVersion){
        return this.start(processDefinitionId, processDefinitionVersion,null);
    }

    @Override
    public ProcessInstance startWith(String deploymentInstanceId, String userId, Map<String, Object> request
                                     ) {
        return this.startWith(deploymentInstanceId,userId,request,null);
    }

    @Override
    public ProcessInstance startWith(String deploymentInstanceId, String userId, Map<String, Object> request,Map<String, Object> response) {
        DeploymentQueryService deploymentQueryService = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getDeploymentQueryService();
        DeploymentInstance deploymentInstance = deploymentQueryService.findById(deploymentInstanceId);

        if(null == request){
            request = new HashMap<String, Object>();
        }

        if(null != userId){
            request.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_START_USER_ID,userId);
        }
        request.put(RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE,deploymentInstance.getProcessDefinitionType());


        ProcessInstance processInstance = this.start(deploymentInstance.getProcessDefinitionId(),
            deploymentInstance.getProcessDefinitionVersion(), request);
        return processInstance;
    }

    @Override
    public ProcessInstance startWith(String deploymentInstanceId, Map<String, Object> request) {
        return startWith(deploymentInstanceId,null, request);
    }

    @Override
    public ProcessInstance startWith(String deploymentInstanceId) {
        return startWith(deploymentInstanceId,null, null);
    }

    @Override
    public void abort(String processInstanceId) {
        this.abort(processInstanceId,"");
    }

    @Override
    public void abort(String processInstanceId, String reason){
        Map<String, Object> request = new HashMap<String, Object>(2);
        request.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_ABORT_REASON,reason);
        abort(processInstanceId,request);

    }

    @Override
    public void abort(String processInstanceId, Map<String, Object> request) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        ProcessInstanceStorage processInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);
        ProcessInstance processInstance = processInstanceStorage.findOne(processInstanceId,processEngineConfiguration );
        processInstance.setStatus(InstanceStatus.aborted);
        String  reason = null;
        if (null != request){
            reason =   String.valueOf(request.get(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_ABORT_REASON)) ;
        }

        processInstance.setReason(reason);
        processInstanceStorage.update(processInstance, processEngineConfiguration);

        ExecutionInstanceStorage executionInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);
        List<ExecutionInstance> executionInstanceList = executionInstanceStorage.findActiveExecution(processInstanceId, processEngineConfiguration);

        if(null != executionInstanceList){
            for (ExecutionInstance executionInstance : executionInstanceList) {
                //TUNE 有点违反在最后去写 DB 的原则。 不过由于这个是终态了，应该不会产生问题。
                MarkDoneUtil.markDoneExecutionInstance(executionInstance,executionInstanceStorage,
                    processEngineConfiguration);
            }
        }

        TaskInstanceStorage taskInstanceStorage = persisterFactoryExtensionPoint.getExtensionPoint(TaskInstanceStorage.class);

        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(processInstanceId);
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
        List<TaskInstance> taskInstanceList = taskInstanceStorage.findTaskList(taskInstanceQueryParam,processEngineConfiguration );
        if(null!=taskInstanceList){
            for (TaskInstance taskInstance : taskInstanceList) {
                if(TaskInstanceConstant.COMPLETED.equals(taskInstance.getStatus()) || TaskInstanceConstant.CANCELED.equals(taskInstance.getStatus())){
                    continue;
                }
                MarkDoneUtil.markDoneTaskInstance(taskInstance, TaskInstanceConstant.ABORTED,TaskInstanceConstant.PENDING, request,
                    taskInstanceStorage, processEngineConfiguration);

            }
        }

    }

}
