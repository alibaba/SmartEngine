package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

import java.util.List;


public interface ExecutionInstanceStorage {

    ExecutionInstance save(ExecutionInstance executionInstance);

    ExecutionInstance find(Long executionInstanceId);

    void remove(Long executionInstanceId);

    List<ExecutionInstance> findActiveExecution(String processInstanceId);

}
