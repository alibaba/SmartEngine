package com.alibaba.smart.framework.engine.service;

import java.util.Map;

public interface TaskService {

    // TaskInstance find(String processInstanceId,String taskId);
    // List<TaskInstance> findByExecution(String processInstanceId,String executionId);
    void complete(String processInstanceId, String taskId, Map<String, Object> variables);
}
