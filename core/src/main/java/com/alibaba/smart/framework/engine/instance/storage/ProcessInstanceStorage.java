package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

import java.util.List;

/** 流程实例存储 */
public interface ProcessInstanceStorage {

    /**
     * 保存流程实例
     *
     * @param processInstance 流程实例
     * @param processEngineConfiguration
     */
    ProcessInstance insert(
            ProcessInstance processInstance, ProcessEngineConfiguration processEngineConfiguration);

    ProcessInstance update(
            ProcessInstance processInstance, ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 加载流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param processEngineConfiguration
     * @return 实例
     */
    ProcessInstance findOne(
            String processInstanceId,
            String tenantId,
            ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 查询流程实例
     *
     * @param instanceId
     * @param processEngineConfiguration
     * @return
     */
    ProcessInstance findOneForUpdate(
            String instanceId,
            String tenantId,
            ProcessEngineConfiguration processEngineConfiguration);

    List<ProcessInstance> queryProcessInstanceList(
            ProcessInstanceQueryParam processInstanceQueryParam,
            ProcessEngineConfiguration processEngineConfiguration);

    Long count(
            ProcessInstanceQueryParam processInstanceQueryParam,
            ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param processEngineConfiguration
     */
    void remove(
            String processInstanceId,
            String tenantId,
            ProcessEngineConfiguration processEngineConfiguration);
}
