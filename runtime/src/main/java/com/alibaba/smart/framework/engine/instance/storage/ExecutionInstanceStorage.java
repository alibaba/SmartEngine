package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;


public interface ExecutionInstanceStorage {

    ExecutionInstance save(ExecutionInstance executionInstance);

    ExecutionInstance find(String executionInstanceId);

    void remove(String executionInstanceId);

}
