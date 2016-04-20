package com.alibaba.smart.framework.engine.instance.store;

import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;

/**
 * 实例存储
 * Created by ettear on 16-4-19.
 */
public interface ExecutionInstanceStorage {
    /**
     * 保存执行实例
     * @param executionInstance 实例
     */
    ExecutionInstance save(ExecutionInstance executionInstance);

    /**
     * 加载执行实例
     * @param processInstanceId 流程实例ID
     * @param executionInstanceId 执行实例ID
     * @return 实例
     */
    ProcessInstance find(String processInstanceId,String executionInstanceId);

    /**
     * 删除执行实例
     * @param processInstanceId 流程实例ID
     * @param executionInstanceId 执行实例ID
     */
    void remove(String processInstanceId,String executionInstanceId);



}
