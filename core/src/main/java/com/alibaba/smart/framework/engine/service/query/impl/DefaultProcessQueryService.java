package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.Collections;
import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.param.query.CompletedProcessQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;

/**
 * Default implementation of ProcessQueryService.
 *
 * @author 高海军 帝奇
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = ProcessQueryService.class)
public class DefaultProcessQueryService implements ProcessQueryService, LifeCycleHook,
        ProcessEngineConfigurationAware {

    private ProcessEngineConfiguration processEngineConfiguration;
    private ProcessInstanceStorage processInstanceStorage;

    @Override
    public void start() {
        this.processInstanceStorage = processEngineConfiguration.getAnnotationScanner()
                .getExtensionPoint(ExtensionConstant.COMMON, ProcessInstanceStorage.class);
    }

    @Override
    public void stop() {
        // Clean up resources if needed
    }

    @Override
    public ProcessInstance findById(String processInstanceId) {
        return findById(processInstanceId, null);
    }

    @Override
    public ProcessInstance findById(String processInstanceId, String tenantId) {
        return processInstanceStorage.findOne(processInstanceId, tenantId, processEngineConfiguration);
    }

    @Override
    public List<ProcessInstance> findList(ProcessInstanceQueryParam processInstanceQueryParam) {
        return processInstanceStorage.queryProcessInstanceList(processInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public Long count(ProcessInstanceQueryParam processInstanceQueryParam) {
        return processInstanceStorage.count(processInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public List<ProcessInstance> findCompletedProcessList(CompletedProcessQueryParam param) {
        if (param == null) {
            return Collections.emptyList();
        }

        ProcessInstanceQueryParam processInstanceQueryParam = convertToProcessInstanceQueryParam(param);
        processInstanceQueryParam.setStatus(InstanceStatus.completed.name());

        return processInstanceStorage.queryProcessInstanceList(processInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public Long countCompletedProcessList(CompletedProcessQueryParam param) {
        if (param == null) {
            return 0L;
        }

        ProcessInstanceQueryParam processInstanceQueryParam = convertToProcessInstanceQueryParam(param);
        processInstanceQueryParam.setStatus(InstanceStatus.completed.name());

        return processInstanceStorage.count(processInstanceQueryParam, processEngineConfiguration);
    }

    /**
     * Convert CompletedProcessQueryParam to ProcessInstanceQueryParam.
     * Note: For completed process query, we only filter by completeTime, not processStartTime.
     */
    private ProcessInstanceQueryParam convertToProcessInstanceQueryParam(CompletedProcessQueryParam param) {
        ProcessInstanceQueryParam processInstanceQueryParam = new ProcessInstanceQueryParam();

        // Pagination
        processInstanceQueryParam.setTenantId(param.getTenantId());
        processInstanceQueryParam.setPageOffset(param.getPageOffset());
        processInstanceQueryParam.setPageSize(param.getPageSize());

        // Query conditions
        processInstanceQueryParam.setStartUserId(param.getStartUserId());
        processInstanceQueryParam.setProcessInstanceIdList(param.getProcessInstanceIdList());
        processInstanceQueryParam.setBizUniqueId(param.getBizUniqueId());

        // Complete time filter (NOT processStartTime - that was a bug)
        processInstanceQueryParam.setCompleteTimeStart(param.getCompleteTimeStart());
        processInstanceQueryParam.setCompleteTimeEnd(param.getCompleteTimeEnd());

        // Process definition type (take first one if multiple provided)
        // TODO: Consider extending ProcessInstanceQueryParam to support multiple types
        if (param.getProcessDefinitionTypes() != null && !param.getProcessDefinitionTypes().isEmpty()) {
            processInstanceQueryParam.setProcessDefinitionType(param.getProcessDefinitionTypes().get(0));
        }

        return processInstanceQueryParam;
    }

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}
