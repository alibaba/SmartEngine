package com.alibaba.smart.framework.engine.common.service;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

import java.util.Map;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  11:13.
 */
public interface TaskAssigneeService {

    void persistTaskAssignee(TaskInstance  taskInstance,Map<String, Object> variables);

    void complete(Long taskInstanceId);
}
