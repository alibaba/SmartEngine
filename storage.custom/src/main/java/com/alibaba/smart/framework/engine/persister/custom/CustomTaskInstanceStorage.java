package com.alibaba.smart.framework.engine.persister.custom;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实例存储 Created by ettear on 16-4-13.
 */
public class CustomTaskInstanceStorage implements TaskInstanceStorage {


    @Override
    public List<TaskInstance> findPendingTask(Long processInstanceId) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public TaskInstance insert(TaskInstance instance) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public TaskInstance update(TaskInstance instance) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public TaskInstance find(Long instanceId) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public void remove(Long instanceId) {
        throw new EngineException("not implement intentionally");
    }
}
