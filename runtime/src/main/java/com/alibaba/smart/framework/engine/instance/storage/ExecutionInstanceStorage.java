package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

import java.util.List;


public interface ExecutionInstanceStorage {

    ExecutionInstance insert(ExecutionInstance executionInstance);

    ExecutionInstance update(ExecutionInstance executionInstance);


    ExecutionInstance find(Long executionInstanceId);

    void remove(Long executionInstanceId);

    List<ExecutionInstance> findActiveExecution(Long processInstanceId);

    List<ExecutionInstance> findAllActiveExecution(Long processInstanceId);

    List<ExecutionInstance> findByActivityInstanceId(Long processInstanceId,Long activityInstanceId);

}
