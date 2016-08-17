package com.alibaba.smart.framework.engine.service;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

public interface TaskService {

    // TaskInstance find(String processInstanceId,String taskId);
    // List<TaskInstance> findByExecution(String processInstanceId,String executionId);
    void complete(String taskId, Map<String, Object> variables);
    
    List<TaskInstance> find(String processInstanceId);

}
