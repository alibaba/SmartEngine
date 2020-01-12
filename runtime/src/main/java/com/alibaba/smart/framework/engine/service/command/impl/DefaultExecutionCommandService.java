package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.ConcurrentException;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;

/**
 * @author 高海军 帝奇  2016.11.11
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = ExecutionCommandService.class)

public class DefaultExecutionCommandService implements ExecutionCommandService, LifeCycleHook,
    ProcessEngineConfigurationAware {

    private ProcessDefinitionContainer processContainer;
    private InstanceContextFactory instanceContextFactory;
    private ProcessEngineConfiguration processEngineConfiguration;

    private  ProcessInstanceStorage processInstanceStorage;
    private ActivityInstanceStorage activityInstanceStorage;
    private ExecutionInstanceStorage executionInstanceStorage;

   private PvmProcessInstance pvmProcessInstance;


    @Override
    public void start() {
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();

        this.processContainer = annotationScanner.getExtensionPoint(ExtensionConstant.SERVICE,ProcessDefinitionContainer.class);
        this.instanceContextFactory = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,InstanceContextFactory.class);

        this. processInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ProcessInstanceStorage.class);
        this. activityInstanceStorage= annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ActivityInstanceStorage.class);
        this. executionInstanceStorage= annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,ExecutionInstanceStorage.class);

        //TODO 单例
        this.pvmProcessInstance = new DefaultPvmProcessInstance();
    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance signal(String executionInstanceId, Map<String, Object> request
                                 ) {
        return this.signal(executionInstanceId,request,null);
    }

    @Override
    public ProcessInstance signal(String executionInstanceId, Map<String, Object> request, Map<String, Object> response) {

        ExecutionInstance executionInstance = queryExecutionInstance(executionInstanceId);

        try {

            PreparePhase preparePhase = new PreparePhase(request, executionInstance).invoke();

            PvmProcessDefinition pvmProcessDefinition = preparePhase.getPvmProcessDefinition();
            ExecutionContext executionContext = preparePhase.getExecutionContext();

            executionContext.setResponse(response);

            String activityId = executionInstance.getProcessDefinitionActivityId();

            PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(activityId);

            ProcessInstance newProcessInstance = pvmProcessInstance.signal(pvmActivity, executionContext);

            CommonServiceHelper.updateAndPersist(executionInstanceId, newProcessInstance, request,processEngineConfiguration);

            return newProcessInstance;
        } finally {
            unLock(processEngineConfiguration, executionInstance.getProcessInstanceId());
        }
    }

    protected ExecutionInstance queryExecutionInstance(String executionInstanceId) {
        ExecutionInstance executionInstance = executionInstanceStorage.find(executionInstanceId, processEngineConfiguration);

        if(null == executionInstance){
            throw new EngineException("No executionInstance found for id "+executionInstanceId);
        }

        if(!executionInstance.isActive()){
            throw new ConcurrentException("The status of signaled executionInstance should be active");

        }
        return executionInstance;
    }

    @Override
    public ProcessInstance signal(String executionInstanceId) {
        return signal(executionInstanceId, null);
    }

    @Override
    public ProcessInstance jumpFrom(String executionInstanceId, String activityId, Map<String, Object> request) {

        ExecutionInstance executionInstance = queryExecutionInstance(executionInstanceId);

        try {
            PreparePhase preparePhase = new PreparePhase(request, executionInstance).invoke();

            PvmProcessDefinition pvmProcessDefinition = preparePhase.getPvmProcessDefinition();
            ExecutionContext executionContext = preparePhase.getExecutionContext();

            PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(activityId);

            MarkDoneUtil.markDoneExecutionInstance(executionInstance, executionInstanceStorage,
                processEngineConfiguration);

            ProcessInstance newProcessInstance = this.pvmProcessInstance.jump(pvmActivity, executionContext);

            CommonServiceHelper.updateAndPersist(executionInstanceId, newProcessInstance, request,
                processEngineConfiguration);

            return newProcessInstance;
        } finally {
            unLock(processEngineConfiguration, executionInstance.getProcessInstanceId());
        }
    }



    public void retry(String processInstanceId, String activityId, ExecutionContext executionContext) {
        ProcessInstance processInstance = processInstanceStorage.findOne(processInstanceId
            , processEngineConfiguration);


        ProcessDefinition  definition = this.processContainer.getProcessDefinition(
            processInstance.getProcessDefinitionIdAndVersion());

        IdBasedElement idBasedElement = definition.getIdBasedElementMap().get(activityId);


        processEngineConfiguration.getDelegationExecutor().execute(executionContext,(Activity)idBasedElement);

    }

    private ExecutionContext createExecutionContext(Map<String, Object> request,
                                                      ProcessEngineConfiguration processEngineConfiguration,
                                                      ExecutionInstance executionInstance,
                                                      ActivityInstance activityInstance,
                                                      ProcessInstance processInstance,
                                                      ProcessDefinition processDefinition) {
        ExecutionContext executionContext = this.instanceContextFactory.create();
        executionContext.setProcessEngineConfiguration(processEngineConfiguration);
        executionContext.setProcessDefinition(processDefinition);
        executionContext.setProcessInstance(processInstance);
        executionContext.setExecutionInstance(executionInstance);
        executionContext.setActivityInstance(activityInstance);
        executionContext.setRequest(request);
        return executionContext;
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

    private class PreparePhase {
        private Map<String, Object> request;
        private ExecutionInstance executionInstance;
        private PvmProcessDefinition pvmProcessDefinition;
        private ExecutionContext executionContext;

        public PreparePhase(Map<String, Object> request, ExecutionInstance executionInstance) {
            this.request = request;
            this.executionInstance = executionInstance;
        }

        public PvmProcessDefinition getPvmProcessDefinition() {
            return pvmProcessDefinition;
        }

        public ExecutionContext getExecutionContext() {
            return executionContext;
        }

        public PreparePhase invoke() {
            //!!! 重要
            tryLock(processEngineConfiguration, executionInstance.getProcessInstanceId());

            //TUNE 校验是否有子流程的执行实例依赖这个父执行实例。

            //BE AWARE: 注意:针对 CUSTOM 场景,由于性能考虑,这里的activityInstance可能为空。调用的地方需要判空。
            ActivityInstance activityInstance = activityInstanceStorage.find(executionInstance.getActivityInstanceId(),
                processEngineConfiguration);

            ProcessInstance processInstance = processInstanceStorage.findOne(executionInstance.getProcessInstanceId()
                , processEngineConfiguration);

            pvmProcessDefinition = DefaultExecutionCommandService.this.processContainer.getPvmProcessDefinition(
                processInstance.getProcessDefinitionIdAndVersion());

            ProcessDefinition processDefinition =
                DefaultExecutionCommandService.this.processContainer.getProcessDefinition(
                processInstance.getProcessDefinitionIdAndVersion());

            executionContext = createExecutionContext(request, processEngineConfiguration,
                executionInstance, activityInstance, processInstance, processDefinition);
            return this;
        }
    }


    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}
