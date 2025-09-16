package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.ExecutionHistoryInstance;

import java.util.List;

/**
 * 执行历史存储
 *
 * @author ettear Created by ettear on 11/10/2017.
 */
public interface ExecutionHistoryInstanceStorage {
    /**
     * 插入
     *
     * @param executionHistoryInstance ExecutionHistoryInstance
     * @param processEngineConfiguration
     * @return ExecutionHistoryInstance
     */
    ExecutionHistoryInstance insert(
            ExecutionHistoryInstance executionHistoryInstance,
            ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 更新
     *
     * @param executionHistoryInstance ExecutionHistoryInstance
     * @param processEngineConfiguration
     * @return ExecutionHistoryInstance
     */
    ExecutionHistoryInstance update(
            ExecutionHistoryInstance executionHistoryInstance,
            ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 获取执行历史
     *
     * @param instanceId 执行历史ID
     * @param processEngineConfiguration
     * @return 执行历史
     */
    ExecutionHistoryInstance find(
            String instanceId,
            String tenantId,
            ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 删除
     *
     * @param instanceId 执行历史ID
     * @param processEngineConfiguration
     */
    void remove(
            String instanceId,
            String tenantId,
            ProcessEngineConfiguration processEngineConfiguration);

    /**
     * 获取流程实例所有执行历史
     *
     * @param processInstanceId 流程实例ID
     * @param processEngineConfiguration
     * @return 流程实例所有执行历史
     */
    List<ExecutionHistoryInstance> findAll(
            String processInstanceId,
            String tenantId,
            ProcessEngineConfiguration processEngineConfiguration);
}
