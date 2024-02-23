package com.alibaba.smart.framework.engine.service.command.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.ContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.exception.ConcurrentException;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
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
    private ContextFactory instanceContextFactory;
    private ProcessEngineConfiguration processEngineConfiguration;

    private ProcessInstanceStorage processInstanceStorage;
    private ActivityInstanceStorage activityInstanceStorage;
    private ExecutionInstanceStorage executionInstanceStorage;

    private PvmProcessInstance pvmProcessInstance;

    @Override
    public void start() {
        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();

        this.processContainer = annotationScanner.getExtensionPoint(ExtensionConstant.SERVICE,
            ProcessDefinitionContainer.class);
        this.instanceContextFactory = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,
            ContextFactory.class);

        this.processInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,
            ProcessInstanceStorage.class);
        this.activityInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,
            ActivityInstanceStorage.class);
        this.executionInstanceStorage = annotationScanner.getExtensionPoint(ExtensionConstant.COMMON,
            ExecutionInstanceStorage.class);

        this.pvmProcessInstance = new DefaultPvmProcessInstance();
    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance signal(String executionInstanceId, Map<String, Object> request) {
        return this.signal(executionInstanceId, request, null);
    }

    @Override
    public ProcessInstance signal(String executionInstanceId, Map<String, Object> request,
                                  Map<String, Object> response) {


        ExecutionInstance executionInstance = queryExecutionInstance(executionInstanceId);

        ProcessInstance processInstance = processInstanceStorage.findOne(executionInstance.getProcessInstanceId()
                , processEngineConfiguration);

            PreparePhase preparePhase = new PreparePhase(request, executionInstance,  processInstance,instanceContextFactory).init();

            PvmProcessDefinition pvmProcessDefinition = preparePhase.getPvmProcessDefinition();
            ExecutionContext executionContext = preparePhase.getExecutionContext();

            executionContext.setResponse(response);

            String activityId = executionInstance.getProcessDefinitionActivityId();

            PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(activityId);

            ProcessInstance newProcessInstance = pvmProcessInstance.signal(pvmActivity, executionContext);

            CommonServiceHelper.createExecution(executionInstanceId, newProcessInstance, request,
                processEngineConfiguration);

            return newProcessInstance;

    }

    @Override
    public ProcessInstance signal(String processInstanceId, String executionInstanceId, Map<String, Object> request,
            Map<String, Object> response) {
        ExecutionInstance executionInstance = queryExecutionInstance(processInstanceId,executionInstanceId);

        ProcessInstance processInstance = processInstanceStorage.findOne(executionInstance.getProcessInstanceId()
                , processEngineConfiguration);



            PreparePhase preparePhase = new PreparePhase(request, executionInstance,  processInstance,instanceContextFactory).initWithShading();

            PvmProcessDefinition pvmProcessDefinition = preparePhase.getPvmProcessDefinition();
            ExecutionContext executionContext = preparePhase.getExecutionContext();

            executionContext.setResponse(response);

            String activityId = executionInstance.getProcessDefinitionActivityId();

            PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(activityId);

            ProcessInstance newProcessInstance = pvmProcessInstance.signal(pvmActivity, executionContext);

            CommonServiceHelper.createExecution(executionInstanceId, newProcessInstance, request,
                    processEngineConfiguration);

            return newProcessInstance;

    }

    protected ExecutionInstance queryExecutionInstance(String processInstanceId, String executionInstanceId) {
        ExecutionInstance executionInstance = executionInstanceStorage.findWithShading(processInstanceId,executionInstanceId,processEngineConfiguration);

        if (null == executionInstance) {
            throw new EngineException("No executionInstance found for id " + executionInstanceId);
        }

        if (!executionInstance.isActive()) {
            throw new ConcurrentException("The status of signaled executionInstance should be active");

        }
        return executionInstance;
    }

    protected ExecutionInstance queryExecutionInstance(String executionInstanceId) {
        ExecutionInstance executionInstance = executionInstanceStorage.find(executionInstanceId,
            processEngineConfiguration);

        if (null == executionInstance) {
            throw new EngineException("No executionInstance found for id " + executionInstanceId);
        }

        if (!executionInstance.isActive()) {
            throw new ConcurrentException("The status of signaled executionInstance should be active");
        }
        return executionInstance;
    }

    @Override
    public ProcessInstance signal(String executionInstanceId) {
        return signal(executionInstanceId, null);
    }

    @Override
    public ProcessInstance jumpFrom(ProcessInstance processInstance, String activityId, String executionInstanceId,
                                    Map<String, Object> request) {

        //NOTATION1：should markDone all active excutioninstances and activityinstances by hands.
        PvmProcessDefinition pvmProcessDefinition = DefaultExecutionCommandService.this.processContainer
            .getPvmProcessDefinition(
                processInstance.getProcessDefinitionIdAndVersion());

        ProcessDefinition processDefinition =
            DefaultExecutionCommandService.this.processContainer.getProcessDefinition(
                processInstance.getProcessDefinitionIdAndVersion());

        //NOTATION2：executionInstance,activityInstance maybe  set to null for jump case
        ExecutionInstance executionInstance = null;
        ActivityInstance activityInstance = null;
        if (null != executionInstanceId) {
            executionInstance = queryExecutionInstance(executionInstanceId);
            //BE AWARE: 注意:针对 CUSTOM 场景,由于性能考虑,这里的activityInstance可能为空。调用的地方需要判空。
            activityInstance = activityInstanceStorage.find(executionInstance.getActivityInstanceId(),
                processEngineConfiguration);
        }

        ExecutionContext executionContext = this.instanceContextFactory.createExecutionContext(request, processEngineConfiguration,
            executionInstance, activityInstance, processInstance, processDefinition);

        PvmActivity pvmActivity = pvmProcessDefinition.getActivities().get(activityId);

        ProcessInstance newProcessInstance = this.pvmProcessInstance.jump(pvmActivity, executionContext);

        //NOTATION3：executionInstance is set to null for jump case
        CommonServiceHelper.createExecution(executionInstanceId, newProcessInstance, request,
            processEngineConfiguration);

        return newProcessInstance;

    }

    @Override
   public ProcessInstance jumpTo(String processInstanceId, String  processDefinitionId, String version,
                                 InstanceStatus instanceStatus, String processDefinitionActivityId) {
        IdGenerator idGenerator = processEngineConfiguration.getIdGenerator();


        ProcessInstance processInstance = new DefaultProcessInstance();
        processInstance.setProcessDefinitionIdAndVersion(processDefinitionId+":"+version);
        processInstance.setProcessDefinitionId(processDefinitionId);
        processInstance.setProcessDefinitionVersion(version);
        processInstance.setStatus(instanceStatus);
        processInstance.setInstanceId(processInstanceId);


        ActivityInstance activityInstance = new DefaultActivityInstance();
        activityInstance.setProcessDefinitionActivityId(processDefinitionActivityId);
        activityInstance.setProcessDefinitionIdAndVersion(processInstance.getProcessDefinitionIdAndVersion());
        activityInstance.setProcessInstanceId(processInstance.getInstanceId());
        idGenerator.generate(activityInstance);


        ExecutionInstance executionInstance = new DefaultExecutionInstance();
        executionInstance.setProcessInstanceId(processInstance.getInstanceId());
        executionInstance.setActivityInstanceId(activityInstance.getInstanceId());
        executionInstance.setProcessDefinitionActivityId(processDefinitionActivityId);
        executionInstance.setProcessDefinitionIdAndVersion(processInstance.getProcessDefinitionIdAndVersion());
        idGenerator.generate(executionInstance);
        executionInstance.setActive(true);

        List<ExecutionInstance> executionInstanceList = new ArrayList<ExecutionInstance>();
        executionInstanceList.add(executionInstance);

        activityInstance.setExecutionInstanceList(executionInstanceList);
        processInstance.getActivityInstances().add(activityInstance);

        CommonServiceHelper.createExecution(executionInstance.getInstanceId(), processInstance, null,
            processEngineConfiguration);

        return processInstance;

    }

    @Override
    public void retry(ProcessInstance processInstance, String activityId, ExecutionContext executionContext) {

        ProcessDefinition definition = this.processContainer.getProcessDefinition(
            processInstance.getProcessDefinitionIdAndVersion());

        IdBasedElement idBasedElement = definition.getIdBasedElementMap().get(activityId);

        processEngineConfiguration.getDelegationExecutor().execute(executionContext, (Activity)idBasedElement);

    }

    @Override
    public void markDone(String executionInstanceId) {
        ExecutionInstance executionInstance = queryExecutionInstance(executionInstanceId);
        MarkDoneUtil.markDoneExecutionInstance(executionInstance, executionInstanceStorage,
            processEngineConfiguration);

    }



    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }

    private class PreparePhase {
        private Map<String, Object> request;
        private ExecutionInstance executionInstance;
        private PvmProcessDefinition pvmProcessDefinition;
        private ExecutionContext executionContext;
        private ProcessInstance processInstance;

        public PreparePhase(Map<String, Object> request, ExecutionInstance executionInstance,ProcessInstance processInstance,ContextFactory instanceContextFactory) {
            this.request = request;
            this.executionInstance = executionInstance;
            this.processInstance = processInstance;
        }

        public PvmProcessDefinition getPvmProcessDefinition() {
            return pvmProcessDefinition;
        }

        public ExecutionContext getExecutionContext() {
            return executionContext;
        }

        public PreparePhase init() {

            //TUNE 校验是否有子流程的执行实例依赖这个父执行实例。

            //BE AWARE: 注意:针对 CUSTOM 场景,由于性能考虑,这里的activityInstance可能为空。调用的地方需要判空。
            ActivityInstance activityInstance = activityInstanceStorage.find(executionInstance.getActivityInstanceId(),
                processEngineConfiguration);



            pvmProcessDefinition = DefaultExecutionCommandService.this.processContainer.getPvmProcessDefinition(
                processInstance.getProcessDefinitionIdAndVersion());

            ProcessDefinition processDefinition =
                DefaultExecutionCommandService.this.processContainer.getProcessDefinition(
                    processInstance.getProcessDefinitionIdAndVersion());

            executionContext = instanceContextFactory.createExecutionContext(request, processEngineConfiguration,
                executionInstance, activityInstance, processInstance, processDefinition);
            return this;
        }

        public PreparePhase initWithShading() {

            //TUNE 校验是否有子流程的执行实例依赖这个父执行实例。
            //BE AWARE: 注意:针对 CUSTOM 场景,由于性能考虑,这里的activityInstance可能为空。调用的地方需要判空。
            ActivityInstance activityInstance = activityInstanceStorage.findWithShading(processInstance.getInstanceId(), executionInstance.getActivityInstanceId(),
                    processEngineConfiguration);
            pvmProcessDefinition = DefaultExecutionCommandService.this.processContainer.getPvmProcessDefinition(
                    processInstance.getProcessDefinitionIdAndVersion());

            ProcessDefinition processDefinition =
                    DefaultExecutionCommandService.this.processContainer.getProcessDefinition(
                            processInstance.getProcessDefinitionIdAndVersion());

            executionContext = instanceContextFactory.createExecutionContext(request, processEngineConfiguration,
                    executionInstance, activityInstance, processInstance, processDefinition);
            return this;
        }
    }

	@Override
    public ExecutionInstance createExecution(ActivityInstance activityInstance) {
		IdGenerator idGenerator = processEngineConfiguration.getIdGenerator();
        ExecutionInstance executionInstance = new DefaultExecutionInstance();
        executionInstance.setProcessInstanceId(activityInstance.getProcessInstanceId());
        executionInstance.setActivityInstanceId(activityInstance.getInstanceId());
        executionInstance.setProcessDefinitionActivityId(activityInstance.getProcessDefinitionActivityId());
        executionInstance.setProcessDefinitionIdAndVersion(activityInstance.getProcessDefinitionIdAndVersion());
        idGenerator.generate(executionInstance);
        executionInstance.setActive(true);
        
        if(CollectionUtil.isNotEmpty(activityInstance.getExecutionInstanceList())) {
        	activityInstance.getExecutionInstanceList().add(executionInstance);
        }
        CommonServiceHelper.createExecution(executionInstance, processEngineConfiguration);
		return executionInstance;
	}
}
