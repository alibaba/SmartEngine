package com.alibaba.smart.framework.engine.persister.custom;

import java.util.List;

import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;

public class CustomTaskAssigneeInstanceStorage implements TaskAssigneeStorage {

    @Override
    public List<TaskAssigneeInstance> findPendingTask(Long processInstanceId) {
        return null;
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
    public TaskAssigneeInstance find(Long taskAssigneeId) {
        return null;
    }

    @Override
    public void remove(Long taskAssigneeId) {

    }
}
