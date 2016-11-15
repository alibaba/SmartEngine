package com.alibaba.smart.framework.engine.modules.storage.memory;

import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class MemoryExecutionInstanceStorage implements ExecutionInstanceStorage {

    private Map<String, ExecutionInstance> instances           = new ConcurrentHashMap<>();

    @Override
    public ExecutionInstance save(ExecutionInstance instance) {
        this.instances.put(instance.getInstanceId(), instance);

        return instance;
    }

    @Override
    public ExecutionInstance find(String instanceId) {
        return this.instances.get(instanceId);
    }



    @Override
    public void remove(String instanceId) {
        this.instances.remove(instanceId);
    }
}
