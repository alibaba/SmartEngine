package com.alibaba.smart.framework.engine.service.query;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

import java.util.List;

/**
 * Created by 高海军 帝奇 74394 on 2016 November  22:08.
 */
public interface TaskQueryService {

    List<TaskInstance> find(String processInstanceId);
}
