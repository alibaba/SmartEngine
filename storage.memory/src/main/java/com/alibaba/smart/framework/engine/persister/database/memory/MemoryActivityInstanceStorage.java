package com.alibaba.smart.framework.engine.persister.database.memory;

import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class MemoryActivityInstanceStorage implements ActivityInstanceStorage {

    private Map<Long, ActivityInstance> instances = new ConcurrentHashMap<>();

    @Override
    public ActivityInstance save(ActivityInstance instance) {
        this.instances.put(instance.getInstanceId(), instance);
        return instance;
    }

    @Override
    public ActivityInstance find(Long instanceId) {
        return this.instances.get(instanceId);
    }


    @Override
    public void remove(Long instanceId) {
        this.instances.remove(instanceId);
    }
}
