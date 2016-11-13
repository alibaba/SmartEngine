package com.alibaba.smart.framework.engine.modules.storage.memory;

import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class MemoryActivityInstanceStorage implements ActivityInstanceStorage {

    private Map<String, ActivityInstance> instances           = new ConcurrentHashMap<>();

    @Override
    public ActivityInstance save(ActivityInstance instance) {
        this.instances.put(instance.getInstanceId(), instance);
        return instance;
    }

    @Override
    public ActivityInstance find(String instanceId) {
        return this.instances.get(instanceId);
    }



    @Override
    public void remove(String instanceId) {
        this.instances.remove(instanceId);
    }
}
