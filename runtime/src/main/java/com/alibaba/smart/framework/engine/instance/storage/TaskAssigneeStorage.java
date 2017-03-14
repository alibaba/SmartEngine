package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.TaskAssignee;

import java.util.List;


public interface TaskAssigneeStorage {

     List<TaskAssignee> findPendingTask(Long processInstanceId);

    TaskAssignee insert(TaskAssignee taskAssignee);

    TaskAssignee update(TaskAssignee taskAssignee);

    TaskAssignee find(Long taskAssigneeId);

    void remove(Long taskAssigneeId);

}
