package com.alibaba.smart.framework.engine.instance.manager;

import com.alibaba.smart.framework.engine.instance.ExecutionInstance;

import java.util.List;

/**
 * Created by ettear on 16-4-18.
 */
public interface ExecutionInstanceManager {

    ExecutionInstance create(String processInstanceId, List<ExecutionInstance> execution);

    ExecutionInstance create(String processInstanceId, String parentId,List<ExecutionInstance> execution);

    void complete(String processInstanceId,String executionInstanceId);

    List<ExecutionInstance> queryAllExecution(String processInstanceId);

    List<ExecutionInstance> queryRunningExecution(String processInstanceId);

}
