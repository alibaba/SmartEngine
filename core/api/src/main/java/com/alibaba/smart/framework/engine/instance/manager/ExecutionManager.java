package com.alibaba.smart.framework.engine.instance.manager;

import java.util.Map;

import com.alibaba.smart.framework.engine.instance.ProcessInstance;

/**
 * Created by ettear on 16-4-19.
 */
public interface ExecutionManager {

    // ExecutionInstance find(String processInstanceId,String executionId);

    // List<ExecutionInstance> findByProcess(String processInstanceId);

    ProcessInstance signal(String processInstanceId, String executionInstanceId, Map<String, Object> variables);
}
