package com.alibaba.smart.framework.engine.service.command;

import java.util.Map;
/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface TaskCommandService {

    // TaskInstance find(String processInstanceId,String taskId);
    // List<TaskInstance> findByExecution(String processInstanceId,String executionId);
    void complete(String taskId, Map<String, Object> variables);
    
    //TODO claim ,vs executionservice 


}