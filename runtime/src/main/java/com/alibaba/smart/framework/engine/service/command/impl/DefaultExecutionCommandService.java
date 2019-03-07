package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.ConcurrentException;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.PersisterFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public class DefaultExecutionCommandService implements ExecutionCommandService, LifeCycleListener {

    private ExtensionPointRegistry extensionPointRegistry;
    private ProcessDefinitionContainer processContainer;
    private InstanceContextFactory instanceContextFactory;

//    private ProcessInstanceStorage processInstanceStorage;
//    private ActivityInstanceStorage activityInstanceStorage;
//    private ExecutionInstanceStorage executionInstanceStorage;


    public DefaultExecutionCommandService(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.processContainer = this.extensionPointRegistry.getExtensionPoint(ProcessDefinitionContainer.class);
        this.instanceContextFactory = this.extensionPointRegistry.getExtensionPoint(InstanceContextFactory.class);

//        this.processInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ProcessInstanceStorage.class);
//        this.activityInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ActivityInstanceStorage.class);
//        this.executionInstanceStorage = this.extensionPointRegistry.getExtensionPoint(ExecutionInstanceStorage.class);

    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance signal(String executionInstanceId, Map<String, Object> request) {
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        //TUNE 可以在对象创建时初始化,但是这里依赖稍微有点问题
        ProcessInstanceStorage processInstanceStorage =persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);
        ActivityInstanceStorage activityInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ActivityInstanceStorage.class);
        ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);

        ExecutionInstance executionInstance = executionInstanceStorage.find(executionInstanceId, processEngineConfiguration);

        if(null == executionInstance){
            throw new EngineException("No executionInstance found for id "+executionInstanceId);
        }

        if(!executionInstance.isActive()){
            throw new ConcurrentException("The status of signaled executionInstance should be active");

        }
        try {

            //!!! 重要
            tryLock(processEngineConfiguration, executionInstance.getProcessInstanceId());

            //TODO 校验是否有子流程的执行实例依赖这个父执行实例。

            //BE AWARE: 注意:针对TP,AliPay场景,由于性能考虑,这里的activityInstance可能为空。调用的地方需要判空。
            ActivityInstance activityInstance = activityInstanceStorage.find(executionInstance.getActivityInstanceId(),
                processEngineConfiguration);

            ProcessInstance processInstance = processInstanceStorage.findOne(executionInstance.getProcessInstanceId(), processEngineConfiguration);

            PvmProcessDefinition pvmProcessDefinition = this.processContainer.getPvmProcessDefinition(
                processInstance.getProcessDefinitionIdAndVersion());
            String processDefinitionActivityId = executionInstance.getProcessDefinitionActivityId();
            PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(processDefinitionActivityId);

            ExecutionContext executionContext = this.instanceContextFactory.create();
            executionContext.setExtensionPointRegistry(this.extensionPointRegistry);
            executionContext.setProcessEngineConfiguration(processEngineConfiguration);
            executionContext.setPvmProcessDefinition(pvmProcessDefinition);
            executionContext.setProcessInstance(processInstance);
            executionContext.setExecutionInstance(executionInstance);
            executionContext.setActivityInstance(activityInstance);
            executionContext.setRequest(request);
            Object processMode = request.get(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_MODE);
            executionContext.setItemApprove(processMode == null ? false : (Boolean)processMode);

            // TUNE 减少不必要的对象创建
            PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

            ProcessInstance newProcessInstance = pvmProcessInstance.signal(pvmActivity, executionContext);

            CommonServiceHelper.updateAndPersist(executionInstanceId, newProcessInstance, request,
                extensionPointRegistry);

            return newProcessInstance;
        } finally {
            unLock(processEngineConfiguration, executionInstance.getProcessInstanceId());
        }
    }



    @Override
    public ProcessInstance signal(String executionInstanceId) {
        return signal(executionInstanceId, null);
    }

    @Override
    public ProcessInstance jump(String executionInstanceId, String activityId, Map<String, Object> request) {
        if (StringUtil.isEmpty(activityId)){
            return signal(executionInstanceId, request);
        }

        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();

        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        //TUNE 可以在对象创建时初始化,但是这里依赖稍微有点问题
        ProcessInstanceStorage processInstanceStorage =persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);
        ActivityInstanceStorage activityInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ActivityInstanceStorage.class);
        ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);

        ExecutionInstance executionInstance = executionInstanceStorage.find(executionInstanceId, processEngineConfiguration);

        if(null == executionInstance){
            throw new EngineException("No executionInstance found for id "+executionInstanceId);
        }

        if(!executionInstance.isActive()){
            throw new EngineException("The status of signaled executionInstance should be active");
        }

        try {
            //!!! 重要
            tryLock(processEngineConfiguration, executionInstance.getProcessInstanceId());

            //TODO 校验是否有子流程的执行实例依赖这个父执行实例。

            //BE AWARE: 注意:针对TP,AliPay场景,由于性能考虑,这里的activityInstance可能为空。调用的地方需要判空。
            ActivityInstance activityInstance = activityInstanceStorage.find(executionInstance.getActivityInstanceId(),
                processEngineConfiguration);

            MarkDoneUtil.markDoneExecutionInstance(executionInstance, executionInstanceStorage,
                processEngineConfiguration);
            ProcessInstance processInstance = processInstanceStorage.findOne(executionInstance.getProcessInstanceId(), processEngineConfiguration);

            PvmProcessDefinition pvmProcessDefinition = this.processContainer.getPvmProcessDefinition(
                processInstance.getProcessDefinitionIdAndVersion());

            PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(activityId);

            ExecutionContext executionContext = this.instanceContextFactory.create();
            executionContext.setExtensionPointRegistry(this.extensionPointRegistry);
            executionContext.setProcessEngineConfiguration(processEngineConfiguration);
            executionContext.setPvmProcessDefinition(pvmProcessDefinition);
            executionContext.setProcessInstance(processInstance);
            executionContext.setExecutionInstance(executionInstance);
            executionContext.setActivityInstance(activityInstance);
            executionContext.setRequest(request);

            // TUNE 减少不必要的对象创建
            PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

            ProcessInstance newProcessInstance = pvmProcessInstance.enter(pvmActivity, executionContext);

            CommonServiceHelper.updateAndPersist(executionInstanceId, newProcessInstance, request,
                extensionPointRegistry);

            return newProcessInstance;
        } finally {
            unLock(processEngineConfiguration, executionInstance.getProcessInstanceId());
        }
    }

    private void tryLock(ProcessEngineConfiguration processEngineConfiguration,
                         String processInstanceId) {
        LockStrategy lockStrategy = processEngineConfiguration.getLockStrategy();
        if (null != lockStrategy) {
            lockStrategy.tryLock(processInstanceId);
        }
    }

    private void unLock(ProcessEngineConfiguration processEngineConfiguration,
                        String processInstanceId) {
        LockStrategy lockStrategy = processEngineConfiguration.getLockStrategy();
        if (null != lockStrategy) {
            lockStrategy.unLock(processInstanceId);
        }
    }

}
