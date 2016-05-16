package com.alibaba.smart.framework.engine.instance.manager;

import java.util.Map;

/**
 * Created by ettear on 16-4-18.
 */
public interface TaskManager {
    //TaskInstance find(String processInstanceId,String taskId);
    //List<TaskInstance> findByExecution(String processInstanceId,String executionId);
    void complete(String processInstanceId,String taskId,Map<String,Object> variables);
}
