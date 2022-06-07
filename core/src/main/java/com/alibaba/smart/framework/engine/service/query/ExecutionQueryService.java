package com.alibaba.smart.framework.engine.service.query;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

/**
 * 查询执行实例。
 *
 * Created by 高海军 帝奇 74394 on 2016 November  22:08.
 */
public interface ExecutionQueryService {

    List<ExecutionInstance> findActiveExecutionList(String processInstanceId);

    /**
     * 默认按照时间降序
     * @param processInstanceId
     * @return
     */
    List<ExecutionInstance> findAll(String processInstanceId);

}
