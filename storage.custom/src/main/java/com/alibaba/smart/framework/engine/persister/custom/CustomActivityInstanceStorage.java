package com.alibaba.smart.framework.engine.persister.custom;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class CustomActivityInstanceStorage implements ActivityInstanceStorage {


    @Override
    public ActivityInstance insert(ActivityInstance instance) {
       return instance;
    }

    @Override
    public ActivityInstance update(ActivityInstance instance) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public ActivityInstance find(Long instanceId) {
        return null;
    }


    @Override
    public void remove(Long instanceId) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public List<ActivityInstance> findAll(Long processInstanceId) {
        throw new EngineException("not implement intentionally");
    }
}
