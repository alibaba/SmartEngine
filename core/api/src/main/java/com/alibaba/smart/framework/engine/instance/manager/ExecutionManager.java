package com.alibaba.smart.framework.engine.instance.manager;

import com.alibaba.smart.framework.engine.instance.ExecutionInstance;

import java.util.List;
import java.util.Map;

/**
 * Created by ettear on 16-4-19.
 */
public interface ExecutionManager {

    //ExecutionInstance find(String processInstanceId,String executionId);

    //List<ExecutionInstance> findByProcess(String processInstanceId);

    void signal(String processInstanceId,String executionId, Map<String, Object> variables);
}
