package com.alibaba.smart.framework.engine.persister.database.memory;

import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class MemoryTaskInstanceStorage implements TaskInstanceStorage {

    private Map<Long, TaskInstance> instances = new ConcurrentHashMap<Long, TaskInstance>();

    @Override
    public List<TaskInstance> findPendingTask(Long processInstanceId) {
        return null;
    }

    @Override
    public TaskInstance insert(TaskInstance instance) {
        this.instances.put(instance.getInstanceId(), instance);

        return instance;
    }

    @Override
    public TaskInstance update(TaskInstance instance) {

        this.instances.put(instance.getInstanceId(), instance);

        return instance;
    }

    @Override
    public TaskInstance find(Long instanceId) {
        return this.instances.get(instanceId);
    }


    @Override
    public void remove(Long instanceId) {
        this.instances.remove(instanceId);
    }
}
