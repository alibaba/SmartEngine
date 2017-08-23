package com.alibaba.smart.framework.engine.persister.memory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class MemoryActivityInstanceStorage implements ActivityInstanceStorage {

    private Map<Long, ActivityInstance> instances = new ConcurrentHashMap<Long, ActivityInstance>();

    @Override
    public ActivityInstance insert(ActivityInstance instance) {
        this.instances.put(instance.getInstanceId(), instance);
        return instance;
    }

    @Override
    public ActivityInstance update(ActivityInstance instance) {
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

    @Override
    public List<ActivityInstance> findAll(Long processInstanceId) {
        ActivityInstance[] activityInstances = instances.values().toArray(new ActivityInstance[]{});
        List<ActivityInstance> activityInstanceList = Arrays.asList(activityInstances);
        return new ArrayList<ActivityInstance>(activityInstanceList);
    }
}
