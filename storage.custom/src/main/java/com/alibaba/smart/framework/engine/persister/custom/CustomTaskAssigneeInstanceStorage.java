package com.alibaba.smart.framework.engine.persister.custom;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;

public class CustomTaskAssigneeInstanceStorage implements TaskAssigneeStorage {

    @Override
    public List<TaskAssigneeInstance> findList(String taskInstanceId) {
        return null;
    }

    @Override
    public Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList) {
        return null;
    }

    @Override
    public TaskAssigneeInstance insert(TaskAssigneeInstance taskAssigneeInstance) {
        return null;
    }

    @Override
    public TaskAssigneeInstance update(String taskAssigneeId, String assigneeId) {
        return null;
    }

    @Override
    public TaskAssigneeInstance findOne(String taskAssigneeId) {
        return null;
    }

    @Override
    public void remove(String taskAssigneeId) {

    }
}
