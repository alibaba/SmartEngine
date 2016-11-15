package com.alibaba.smart.framework.engine.modules.storage.memory;

import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class MemoryTaskInstanceStorage implements TaskInstanceStorage {

    private Map<String, TaskInstance> instances           = new ConcurrentHashMap<>();

    @Override
    public TaskInstance save(TaskInstance instance) {
        this.instances.put(instance.getInstanceId(), instance);

        return instance;
    }

    @Override
    public TaskInstance find(String instanceId) {
        return this.instances.get(instanceId);
    }



    @Override
    public void remove(String instanceId) {
        this.instances.remove(instanceId);
    }
}
