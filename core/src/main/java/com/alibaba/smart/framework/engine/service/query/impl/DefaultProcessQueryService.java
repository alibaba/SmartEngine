package com.alibaba.smart.framework.engine.service.query.impl;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.CompletedProcessQueryParam;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:10.
 */

@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = ProcessQueryService.class)

public class DefaultProcessQueryService implements ProcessQueryService, LifeCycleHook ,
    ProcessEngineConfigurationAware {

    private ProcessInstanceStorage processInstanceStorage;



    @Override
    public void start() {

        this.processInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(ExtensionConstant.COMMON,ProcessInstanceStorage.class);


    }

    @Override
    public void stop() {

    }

    @Override
    public ProcessInstance findById(String processInstanceId) {

        return findById(processInstanceId,null);
    }

    @Override
    public ProcessInstance findById(String processInstanceId,String tenantId) {

        return processInstanceStorage.findOne(processInstanceId,tenantId, processEngineConfiguration);
    }

    @Override
    public List<ProcessInstance> findList(ProcessInstanceQueryParam processInstanceQueryParam) {

        return processInstanceStorage.queryProcessInstanceList(processInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public Long count(ProcessInstanceQueryParam processInstanceQueryParam) {

        return processInstanceStorage.count(processInstanceQueryParam,processEngineConfiguration );
    }

    @Override
    public List<ProcessInstance> findCompletedProcessList(CompletedProcessQueryParam param) {
        // 将CompletedProcessQueryParam转换为ProcessInstanceQueryParam
        ProcessInstanceQueryParam processInstanceQueryParam = convertToProcessInstanceQueryParam(param);
        processInstanceQueryParam.setStatus(InstanceStatus.completed.name());
        
        return processInstanceStorage.queryProcessInstanceList(processInstanceQueryParam, processEngineConfiguration);
    }

    @Override
    public Long countCompletedProcessList(CompletedProcessQueryParam param) {
        // 将CompletedProcessQueryParam转换为ProcessInstanceQueryParam
        ProcessInstanceQueryParam processInstanceQueryParam = convertToProcessInstanceQueryParam(param);
        processInstanceQueryParam.setStatus(InstanceStatus.completed.name());
        
        return processInstanceStorage.count(processInstanceQueryParam, processEngineConfiguration);
    }

    /**
     * 将CompletedProcessQueryParam转换为ProcessInstanceQueryParam
     */
    private ProcessInstanceQueryParam convertToProcessInstanceQueryParam(CompletedProcessQueryParam param) {
        ProcessInstanceQueryParam processInstanceQueryParam = new ProcessInstanceQueryParam();
        
        processInstanceQueryParam.setTenantId(param.getTenantId());
        processInstanceQueryParam.setPageOffset(param.getPageOffset());
        processInstanceQueryParam.setPageSize(param.getPageSize());
        
        processInstanceQueryParam.setStartUserId(param.getStartUserId());
        processInstanceQueryParam.setProcessInstanceIdList(param.getProcessInstanceIdList());
        processInstanceQueryParam.setBizUniqueId(param.getBizUniqueId());
        processInstanceQueryParam.setProcessStartTime(param.getCompleteTimeStart());
        processInstanceQueryParam.setProcessEndTime(param.getCompleteTimeEnd());
        
        // 处理流程定义类型
        if (param.getProcessDefinitionTypes() != null && !param.getProcessDefinitionTypes().isEmpty()) {
            // 这里简化处理，取第一个类型，实际可能需要扩展ProcessInstanceQueryParam支持多个类型
            processInstanceQueryParam.setProcessDefinitionType(param.getProcessDefinitionTypes().get(0));
        }
        
        return processInstanceQueryParam;
    }

    private ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
}
