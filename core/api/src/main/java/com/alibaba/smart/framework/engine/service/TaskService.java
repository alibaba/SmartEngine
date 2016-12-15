package com.alibaba.smart.framework.engine.service;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

import java.util.List;
import java.util.Map;

public interface TaskService {

    // TaskInstance find(String processInstanceId,String taskId);
    // List<TaskInstance> findByExecution(String processInstanceId,String executionId);
    void complete(String taskId, Map<String, Object> variables);
    
    //TODO claim ,vs executionservice 
    
    List<TaskInstance> find(String processInstanceId);

}
