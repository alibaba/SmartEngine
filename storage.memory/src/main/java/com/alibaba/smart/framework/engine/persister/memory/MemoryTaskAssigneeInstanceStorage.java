package com.alibaba.smart.framework.engine.persister.memory;

import java.util.List;

import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;

public class MemoryTaskAssigneeInstanceStorage implements TaskAssigneeStorage {

    @Override
    public List<TaskAssigneeInstance> findList(Long taskInstanceId) {
        return  null;
    }

    @Override
    public TaskAssigneeInstance insert(TaskAssigneeInstance taskAssigneeInstance) {
        return null;
    }

    @Override
    public TaskAssigneeInstance update(Long taskAssigneeId, String assigneeId) {
        return null;
    }

    @Override
    public TaskAssigneeInstance findOne(Long taskAssigneeId) {
        return null;
    }

    @Override
    public void remove(Long taskAssigneeId) {

    }
}
