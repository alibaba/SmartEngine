package com.alibaba.smart.framework.engine.persister.database.memory;

import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class MemoryExecutionInstanceStorage implements ExecutionInstanceStorage {

    private Map<Long, ExecutionInstance> instances = new ConcurrentHashMap<>();

    @Override
    public ExecutionInstance insert(ExecutionInstance instance) {
        this.instances.put(instance.getInstanceId(), instance);

        return instance;
    }

    @Override
    public ExecutionInstance update(ExecutionInstance executionInstance) {
        return null;
    }

    @Override
    public ExecutionInstance find(Long instanceId) {
        return this.instances.get(instanceId);
    }


    @Override
    public void remove(Long instanceId) {
        this.instances.remove(instanceId);
    }

    @Override
    public List<ExecutionInstance> findActiveExecution(String processInstanceId) {
        return null;
    }
}