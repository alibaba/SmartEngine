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
import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.param.EngineParam;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;
import com.alibaba.smart.framework.engine.pvm.PvmProcessDefinition;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.google.common.collect.Maps;
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
    public void abort(String processInstanceId) {

    }

    @Override
    public ProcessInstance find(String processInstanceId) {
        return this.processInstanceStorage.find(processInstanceId);
    }

    @Override
    public ProcessInstance recovery(EngineParam engineParam) {


        ProcessInstance processInstance = this.processInstanceFactory.recovery(engineParam.getProcessParam());
        ProcessInstance finalProcessInstance = processInstance;
        engineParam.getExecutionParams().stream().forEach(
                p->{
                    ExecutionInstance executionInstance = this.executionInstanceFactory.recovery(p);

                    ActivityInstance activityInstance = this.activityInstanceFactory.recovery(
                            engineParam.getActivityParams().stream().filter(
                                    activityParam->activityParam.getActivityId().equals(p.getActivityId())
                            ).findFirst().orElseThrow(()->new EngineException("not assign activity"))
                    );
                    executionInstance.setActivity(activityInstance);
                    if (!finalProcessInstance.getInstanceId().equals(executionInstance.getProcessInstanceId())
                            || !executionInstance.getProcessInstanceId().equals(activityInstance.getProcessInstanceId())) {

                        throw new EngineException("recovery instance is not right!");
                    }
                    finalProcessInstance.addExecution(executionInstance);
                }
        );

        PvmProcessDefinition processDefinition = this.processDefinitionContainer.get(
                engineParam.getProcessParam().getProcessDefationId(),
                engineParam.getProcessParam().getProcessDefationVersion());

        if (null == processDefinition) {
            throw new EngineException("can not find process defiation");
        }

        processInstance.setProcessUri(processDefinition.getUri());
        processInstanceStorage.save(processInstance);
        return processInstance;
    }

    @Override
    public ProcessInstance run(ProcessDefinition definition,String instanceId, String activityId, boolean sub,Map<String,Object> request) {


        ProcessInstance processInstance = getProcessInstance(instanceId,sub);
        PvmProcessInstance pvmProcess = new DefaultPvmProcessInstance();
        PvmProcessDefinition pvmProcessDefinition = this.processDefinitionContainer.get(definition.getId(), definition.getVersion());
        ExecutionInstance chosenExecution  = null;
        for (ExecutionInstance executionInstance : processInstance.getExecutions().values()) {
            if (StringUtils.equalsIgnoreCase(executionInstance.getActivity().getActivityId(),activityId)) {
                chosenExecution = executionInstance;
                break;
            }
            checkAlreadyProcessed(activityId,pvmProcessDefinition,executionInstance.getActivity().getActivityId());
        }
        if (chosenExecution == null) {
            throw new EngineException("not find activiy,check process defintion");
        }
        ExecutionContext instanceContext = this.instanceContextFactory.create();
        instanceContext.setProcessInstance(processInstance);
        instanceContext.setCurrentExecution(chosenExecution);// 执行实例添加到当前上下文中
        instanceContext.setRequest(request);
        instanceContext.setPvmProcessDefinition(pvmProcessDefinition);
        pvmProcess.run(instanceContext);
        return processInstance;
    }


    @Override
    public ProcessInstance pushActivityOnRam(ProcessDefinition definition,String processId) {
        ProcessInstance processInstance = getProcessInstance(processId,false);
        PvmProcessInstance pvmProcess = new DefaultPvmProcessInstance();
        PvmProcessDefinition pvmProcessDefinition = this.processDefinitionContainer.get(definition.getId(), definition.getVersion());

        for (ExecutionInstance executionInstance :processInstance.getExecutions().values()) {
            ExecutionContext instanceContext = this.instanceContextFactory.create();
            instanceContext.setProcessInstance(processInstance);
            instanceContext.setCurrentExecution(executionInstance);
            instanceContext.setPvmProcessDefinition(pvmProcessDefinition);
            instanceContext.changePushMod(true);
            instanceContext.setRequest(Maps.newHashMap());
            pvmProcess.run(instanceContext);
        }

        return processInstance;


    }







    private void checkAlreadyProcessed(String assignId, PvmProcessDefinition pvmProcessDefinition, String currentId) {
        PvmActivity currentActivity = findCurrentAcitivy(pvmProcessDefinition,currentId);
        if (currentActivity == null) {
            return;
        }
        if (checkIsAfter(currentActivity,assignId)) {
            throw new EngineException("assign acitivy is on next");
        }else if (checkIsBefore(currentActivity,assignId)) {
            throw new EngineException("assign acitivy is on before");
        }
    }

    private PvmActivity findCurrentAcitivy(PvmProcessDefinition pvmProcessDefinition, String currentId) {
        if (!pvmProcessDefinition.getActivities().containsKey(currentId)) {
            throw new EngineException("can not find assign activity : "+currentId+"");
        }
        PvmActivity currentAt = pvmProcessDefinition.getActivities().get(currentId);
        /**
         * 这里主要是解决图论中无法查找多点问题,解决方式有点丑
         */
        if (currentAt.getModel().getId().equals("joinGw") || currentAt.getModel().getId().equals("forkGw")) {
            return null;
        }
        return currentAt;
    }

    private boolean checkIsBefore(PvmActivity currentActivity, String assignId) {
        for (PvmTransition transition:currentActivity.getIncomeTransitions().values()) {
            return transition.getSource().getModel().getId().equals(assignId) || checkIsBefore(transition.getSource(), assignId);
        }
        return false;
    }






    private boolean checkIsAfter(PvmActivity currentActivity, String assignId) {
        for (PvmTransition transition:currentActivity.getOutcomeTransitions().values()) {
            return transition.getTarget().getModel().getId().equals(assignId) || checkIsAfter(transition.getTarget(), assignId);
        }
        return false;
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




}
