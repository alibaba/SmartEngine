package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

import java.util.List;


public interface ExecutionInstanceStorage {

    ExecutionInstance insert(ExecutionInstance executionInstance);

    ExecutionInstance update(ExecutionInstance executionInstance);


    ExecutionInstance find(String executionInstanceId);

    void remove(String executionInstanceId);

    List<ExecutionInstance> findActiveExecution(String processInstanceId);

    List<ExecutionInstance> findByActivityInstanceId(String processInstanceId,String activityInstanceId);

}
