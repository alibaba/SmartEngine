package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
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

import java.util.Date;
import java.util.Map;

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
    public ProcessInstance signal(Long executionInstanceId, Map<String, Object> request) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        //TUNE 可以在对象创建时初始化,但是这里依赖稍微有点问题
        ProcessInstanceStorage processInstanceStorage =persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);
        ActivityInstanceStorage activityInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ActivityInstanceStorage.class);
        ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);

        ExecutionInstance executionInstance = executionInstanceStorage.find(executionInstanceId);

        if(null == executionInstance){
            throw new EngineException("No executionInstance found for id "+executionInstanceId);
        }

        if(!executionInstance.isActive()){
            throw new EngineException("The status of signaled executionInstance should be active");

        }


        //注意:针对TP,AliPay场景,由于性能考虑,这里的activityInstance可能为空。调用的地方需要判空。
        ActivityInstance activityInstance= activityInstanceStorage.find(executionInstance.getActivityInstanceId());

        ProcessInstance processInstance = processInstanceStorage.find(executionInstance.getProcessInstanceId());

        PvmProcessDefinition pvmProcessDefinition = this.processContainer.get(processInstance.getProcessDefinitionIdAndVersion());
        String activityId = executionInstance.getActivityId();
        PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(activityId);


        ExecutionContext executionContext = this.instanceContextFactory.create();
        executionContext.setExtensionPointRegistry(this.extensionPointRegistry);
        ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();
        executionContext.setProcessEngineConfiguration(processEngineConfiguration);
        executionContext.setPvmProcessDefinition(pvmProcessDefinition);
        executionContext.setProcessInstance(processInstance);
        executionContext.setRequest(request);
        if(null != activityInstance){
            executionContext.setBlockId(activityInstance.getBlockId());
        }



        // TUNE 减少不必要的对象创建
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

        markDone(activityInstance,executionInstance);


        ProcessInstance newProcessInstance = pvmProcessInstance.signal(pvmActivity, executionContext);


        persist(newProcessInstance,  request);

        return newProcessInstance;
    }

    @Override
    public ProcessInstance signal(Long executionInstanceId) {
        return signal( executionInstanceId, null);
    }


    private ProcessInstance persist(ProcessInstance processInstance,Map<String, Object> request) {

            ProcessEngineConfiguration processEngineConfiguration = extensionPointRegistry.getExtensionPoint(SmartEngine.class).getProcessEngineConfiguration();

            ProcessInstance newProcessInstance =  defaultPersisteInstance1(processInstance, request, processEngineConfiguration);

            return newProcessInstance;
        }

    private ProcessInstance defaultPersisteInstance1(ProcessInstance processInstance, Map<String, Object> request,ProcessEngineConfiguration processEngineConfiguration) {
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);

        //TUNE 可以在对象创建时初始化,但是这里依赖稍微优化下。
        ProcessInstanceStorage processInstanceStorage =persisterFactoryExtensionPoint.getExtensionPoint(ProcessInstanceStorage.class);

        ProcessInstance newProcessInstance=   processInstanceStorage.update(processInstance);
        CommonServiceHelper.persist(processInstance, request, processEngineConfiguration,  persisterFactoryExtensionPoint);

        return newProcessInstance;
    }


    private  void markDone(ActivityInstance activityInstance,ExecutionInstance executionInstance) {
        Date completeDate = DateUtil.getCurrentDate();
        executionInstance.setCompleteDate(completeDate);
        executionInstance.setActive(false);
        if(null != activityInstance){
            activityInstance.setCompleteDate(completeDate);
            //TODO
            //activityInstance.setActive(false);
        }

        //TODO 这里可以把需要更新的对象放到另外一个队列中去,后面再统一更新。 需要结合 CustomExecutionInstanceStorage#Update 一起看下。
        PersisterFactoryExtensionPoint persisterFactoryExtensionPoint = this.extensionPointRegistry.getExtensionPoint(PersisterFactoryExtensionPoint.class);
        ExecutionInstanceStorage executionInstanceStorage=persisterFactoryExtensionPoint.getExtensionPoint(ExecutionInstanceStorage.class);

        executionInstanceStorage.update(executionInstance);

    }

}
