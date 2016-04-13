package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.instance.Instance;
import com.alibaba.smart.framework.engine.instance.InstanceStore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储
 * Created by ettear on 16-4-13.
 */
public class MemoryInstanceStore implements InstanceStore {

    private Map<String, Instance> instances = new ConcurrentHashMap<>();

    @Override
    public void save(Instance instance) {
        this.instances.put(instance.getId(),instance);
    }

    @Override
    public Instance load(String instanceId) {
        return this.instances.get(instanceId);
    }

    @Override
    public void remove(String instanceId) {
        this.instances.remove(instanceId);
    }
}
