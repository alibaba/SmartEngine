package com.alibaba.smart.framework.engine.persister.database.memory;

import com.alibaba.smart.framework.engine.instance.storage.ProcessInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class MemoryProcessInstanceStorage implements ProcessInstanceStorage {

    private Map<Long, ProcessInstance> instances = new ConcurrentHashMap<>();

    @Override
    public ProcessInstance insert(ProcessInstance instance) {
        this.instances.put(instance.getInstanceId(), instance);

        return instance;
    }

    @Override
    public ProcessInstance update(ProcessInstance processInstance) {
        return null;
    }

    @Override
    public ProcessInstance find(Long instanceId) {
        return this.instances.get(instanceId);
    }


    @Override
    public void remove(Long instanceId) {
        this.instances.remove(instanceId);
    }
}
