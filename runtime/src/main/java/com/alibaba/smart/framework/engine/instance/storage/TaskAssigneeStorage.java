package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;

import java.util.List;


public interface TaskAssigneeStorage {

    List<TaskAssigneeInstance> findPendingTask(Long processInstanceId);

    TaskAssigneeInstance insert(TaskAssigneeInstance taskAssigneeInstance);

    TaskAssigneeInstance update(TaskAssigneeInstance taskAssigneeInstance);

    TaskAssigneeInstance find(Long taskAssigneeId);

    void remove(Long taskAssigneeId);

}
