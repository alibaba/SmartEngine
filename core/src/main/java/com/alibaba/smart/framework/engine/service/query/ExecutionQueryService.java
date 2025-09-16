package com.alibaba.smart.framework.engine.service.query;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

import java.util.List;

/**
 * 查询执行实例。
 *
 * <p>Created by 高海军 帝奇 74394 on 2016 November 22:08.
 */
public interface ExecutionQueryService {

    List<ExecutionInstance> findActiveExecutionList(String processInstanceId, String tenantId);

    List<ExecutionInstance> findActiveExecutionList(String processInstanceId);

    /**
     * 默认按照时间降序
     *
     * @param processInstanceId
     * @return
     */
    List<ExecutionInstance> findAll(String processInstanceId);

    List<ExecutionInstance> findAll(String processInstanceId, String tenantId);
}
