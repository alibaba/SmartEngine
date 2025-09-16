package com.alibaba.smart.framework.engine.service.command.impl;

import com.alibaba.smart.framework.engine.common.util.MarkDoneUtil;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.ContextFactory;
import com.alibaba.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.factory.ProcessInstanceFactory;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.pvm.PvmProcessInstance;
import com.alibaba.smart.framework.engine.pvm.impl.DefaultPvmProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.DeploymentQueryService;
import com.alibaba.smart.framework.engine.util.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 高海军 帝奇 2016.11.11
 * @author ettear 2016.04.13
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = ProcessCommandService.class)
public class DefaultProcessCommandService
        implements ProcessCommandService, LifeCycleHook, ProcessEngineConfigurationAware {

    private ProcessDefinitionContainer processDefinitionContainer;

    private ContextFactory instanceContextFactory;
    private ProcessInstanceFactory processInstanceFactory;

    @Override
    public void start() {

        AnnotationScanner annotationScanner = processEngineConfiguration.getAnnotationScanner();

        this.processDefinitionContainer =
                annotationScanner.getExtensionPoint(
                        ExtensionConstant.SERVICE, ProcessDefinitionContainer.class);

        this.instanceContextFactory =
                annotationScanner.getExtensionPoint(ExtensionConstant.COMMON, ContextFactory.class);
        this.processInstanceFactory =
                annotationScanner.getExtensionPoint(
                        ExtensionConstant.COMMON, ProcessInstanceFactory.class);
        this.processInstanceStorage =
                annotationScanner.getExtensionPoint(
                        ExtensionConstant.COMMON, ProcessInstanceStorage.class);

        this.taskInstanceStorage =
                annotationScanner.getExtensionPoint(
                        ExtensionConstant.COMMON, TaskInstanceStorage.class);
        this.executionInstanceStorage =
                annotationScanner.getExtensionPoint(
                        ExtensionConstant.COMMON, ExecutionInstanceStorage.class);
    }

    @Override
    public void stop() {}

    @Override
    public ProcessInstance start(
            String processDefinitionId,
            String processDefinitionVersion,
            Map<String, Object> request) {
        return this.start(processDefinitionId, processDefinitionVersion, request, null);
    }

    @Override
    public ProcessInstance start(
            String processDefinitionId,
            String processDefinitionVersion,
            Map<String, Object> request,
            Map<String, Object> response) {
        ProcessInstance processInstance =
                processInstanceFactory.create(
                        processEngineConfiguration,
                        processDefinitionId,
                        processDefinitionVersion,
                        request);

        ExecutionContext executionContext =
                this.instanceContextFactory.createProcessContext(
                        processEngineConfiguration, processInstance, request, response, null);

        // TUNE 减少不必要的对象创建
        PvmProcessInstance pvmProcessInstance = new DefaultPvmProcessInstance();

        processInstance = pvmProcessInstance.start(executionContext);

        processInstance =
                CommonServiceHelper.insertAndPersist(
                        processInstance, request, processEngineConfiguration);

        return processInstance;
    }

    @Override
    public ProcessInstance start(String processDefinitionId, String processDefinitionVersion) {
        String tenantId = null;
        return this.start(processDefinitionId, processDefinitionVersion, tenantId);
    }

    @Override
    public ProcessInstance start(
            String processDefinitionId, String processDefinitionVersion, String tenantId) {
        Map<String, Object> request = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(tenantId)) {
            request.put(RequestMapSpecialKeyConstant.TENANT_ID, tenantId);
        }
        return this.start(processDefinitionId, processDefinitionVersion, request);
    }

    @Override
    public ProcessInstance startWith(
            String deploymentInstanceId, String userId, Map<String, Object> request) {
        return this.startWith(deploymentInstanceId, userId, request, null);
    }

    @Override
    public ProcessInstance startWith(
            String deploymentInstanceId,
            String userId,
            Map<String, Object> request,
            Map<String, Object> response) {
        String tenantId = null;
        if (null != request) {
            tenantId = ObjectUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TENANT_ID));
        }
        DeploymentQueryService deploymentQueryService =
                processEngineConfiguration.getSmartEngine().getDeploymentQueryService();
        DeploymentInstance deploymentInstance =
                deploymentQueryService.findById(deploymentInstanceId, tenantId);

        if (null == request) {
            request = new HashMap<String, Object>();
        }

        if (null != userId) {
            request.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_START_USER_ID, userId);
        }
        request.put(
                RequestMapSpecialKeyConstant.PROCESS_DEFINITION_TYPE,
                deploymentInstance.getProcessDefinitionType());

        ProcessInstance processInstance =
                this.start(
                        deploymentInstance.getProcessDefinitionId(),
                        deploymentInstance.getProcessDefinitionVersion(),
                        request);
        return processInstance;
    }

    @Override
    public ProcessInstance startWith(String deploymentInstanceId, Map<String, Object> request) {
        return startWith(deploymentInstanceId, null, request);
    }

    @Override
    public ProcessInstance startWith(String deploymentInstanceId) {
        String tenantId = null;
        return startWith(deploymentInstanceId, tenantId);
    }

    @Override
    public ProcessInstance startWith(String deploymentInstanceId, String tenantId) {
        Map<String, Object> request = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(tenantId)) {
            request.put(RequestMapSpecialKeyConstant.TENANT_ID, tenantId);
        }
        return startWith(deploymentInstanceId, null, request);
    }

    @Override
    public void abort(String processInstanceId) {
        this.abort(processInstanceId, "", null);
    }

    @Override
    public void abort(String processInstanceId, String reason) {
        this.abort(processInstanceId, reason, null);
    }

    @Override
    public void abort(String processInstanceId, String reason, String tenantId) {
        Map<String, Object> request = new HashMap<String, Object>(2);
        request.put(RequestMapSpecialKeyConstant.PROCESS_INSTANCE_ABORT_REASON, reason);

        if (StringUtil.isNotEmpty(tenantId)) {
            request.put(RequestMapSpecialKeyConstant.TENANT_ID, tenantId);
        }
        abort(processInstanceId, request);
    }

    @Override
    public void abort(String processInstanceId, Map<String, Object> request) {
        String tenantId = null;
        if (null != request) {
            tenantId = ObjectUtil.obj2Str(request.get(RequestMapSpecialKeyConstant.TENANT_ID));
        }
        ProcessInstance processInstance =
                processInstanceStorage.findOne(
                        processInstanceId, tenantId, processEngineConfiguration);
        processInstance.setStatus(InstanceStatus.aborted);
        String reason = null;
        if (null != request) {
            reason =
                    ObjectUtil.obj2Str(
                            request.get(
                                    RequestMapSpecialKeyConstant.PROCESS_INSTANCE_ABORT_REASON));
        }

        processInstance.setReason(reason);
        processInstanceStorage.update(processInstance, processEngineConfiguration);

        List<ExecutionInstance> executionInstanceList =
                executionInstanceStorage.findActiveExecution(
                        processInstanceId, tenantId, processEngineConfiguration);

        if (null != executionInstanceList) {
            for (ExecutionInstance executionInstance : executionInstanceList) {
                // TUNE 有点违反在最后去写 DB 的原则。 不过由于这个是终态了，应该不会产生问题。
                MarkDoneUtil.markDoneExecutionInstance(
                        executionInstance, executionInstanceStorage, processEngineConfiguration);
            }
        }

        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(processInstanceId);
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
        List<TaskInstance> taskInstanceList =
                taskInstanceStorage.findTaskList(
                        taskInstanceQueryParam, processEngineConfiguration);
        if (null != taskInstanceList) {
            for (TaskInstance taskInstance : taskInstanceList) {
                if (TaskInstanceConstant.COMPLETED.equals(taskInstance.getStatus())
                        || TaskInstanceConstant.CANCELED.equals(taskInstance.getStatus())) {
                    continue;
                }
                MarkDoneUtil.markDoneTaskInstance(
                        taskInstance,
                        TaskInstanceConstant.ABORTED,
                        TaskInstanceConstant.PENDING,
                        request,
                        taskInstanceStorage,
                        processEngineConfiguration);
            }
        }
    }

    private ProcessEngineConfiguration processEngineConfiguration;
    private ProcessInstanceStorage processInstanceStorage;
    private TaskInstanceStorage taskInstanceStorage;
    private ExecutionInstanceStorage executionInstanceStorage;

    @Override
    public void setProcessEngineConfiguration(
            ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}
