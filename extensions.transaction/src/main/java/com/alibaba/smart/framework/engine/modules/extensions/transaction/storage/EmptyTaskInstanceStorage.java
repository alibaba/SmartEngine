package com.alibaba.smart.framework.engine.modules.extensions.transaction.storage;

import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

import java.util.List;

/**
 * @author Leo.yy   Created on 2017/8/3.
 * @description
 * @see
 */
public class EmptyTaskInstanceStorage implements TaskInstanceStorage {
    @Override
    public List<TaskInstance> findPendingTask(Long processInstanceId) {
        return null;
    }

    @Override
    public TaskInstance insert(TaskInstance taskInstance) {
        return null;
    }

    @Override
    public TaskInstance update(TaskInstance taskInstance) {
        return null;
    }

    @Override
    public TaskInstance find(Long taskInstanceId) {
        return null;
    }

    @Override
    public void remove(Long taskInstanceId) {

    }
}
