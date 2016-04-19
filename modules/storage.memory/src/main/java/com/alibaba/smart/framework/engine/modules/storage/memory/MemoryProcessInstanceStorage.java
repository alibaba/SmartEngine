package com.alibaba.smart.framework.engine.modules.storage.memory;

import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.store.ProcessInstanceStorage;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储
 * Created by ettear on 16-4-13.
 */
public class MemoryProcessInstanceStorage implements ProcessInstanceStorage {

    private Map<String, ProcessInstance> instances = new ConcurrentHashMap<>();

    @Override
    public ProcessInstance save(ProcessInstance instance) {
        instance.setId(UUID.randomUUID().toString());
        this.instances.put(instance.getId(), instance);
        return instance;
    }

    @Override
    public ProcessInstance load(String instanceId) {
        return this.instances.get(instanceId);
    }

    @Override
    public void remove(String instanceId) {
        this.instances.remove(instanceId);
    }
}
