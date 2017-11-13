package com.alibaba.smart.framework.engine.service.query;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

import java.util.List;

/**
 * 查询执行实例。
 *
 * Created by 高海军 帝奇 74394 on 2016 November  22:08.
 */
public interface ExecutionQueryService {

    List<ExecutionInstance> findActiveExecutionList(Long processInstanceId);

}
