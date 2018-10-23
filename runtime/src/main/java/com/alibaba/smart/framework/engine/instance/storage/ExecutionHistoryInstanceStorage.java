package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.ExecutionHistoryInstance;

/**
 * 执行历史存储
 * @author ettear
 * Created by ettear on 11/10/2017.
 */
public interface ExecutionHistoryInstanceStorage {
    /**
     * 插入
     * @param executionHistoryInstance ExecutionHistoryInstance
     * @return ExecutionHistoryInstance
     */
    ExecutionHistoryInstance insert(ExecutionHistoryInstance executionHistoryInstance);

    /**
     * 更新
     * @param executionHistoryInstance ExecutionHistoryInstance
     * @return ExecutionHistoryInstance
     */
    ExecutionHistoryInstance update(ExecutionHistoryInstance executionHistoryInstance);

    /**
     * 获取执行历史
     * @param instanceId 执行历史ID
     * @return 执行历史
     */
    ExecutionHistoryInstance find(String instanceId);

    /**
     * 删除
     * @param instanceId 执行历史ID
     */
    void remove(String instanceId);

    /**
     * 获取流程实例所有执行历史
     * @param processInstanceId 流程实例ID
     * @return 流程实例所有执行历史
     */
    List<ExecutionHistoryInstance> findAll(String processInstanceId);

}
