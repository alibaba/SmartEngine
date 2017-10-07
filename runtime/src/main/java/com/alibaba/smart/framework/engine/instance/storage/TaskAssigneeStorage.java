package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

import java.util.List;


public interface TaskAssigneeStorage {

    List<TaskAssigneeInstance> findPendingTask(Long processInstanceId);

    TaskAssigneeInstance insert( TaskAssigneeInstance taskAssigneeInstance);

    TaskAssigneeInstance update(Long taskAssigneeId,String assigneeId);

    TaskAssigneeInstance find(Long taskAssigneeId);

    void remove(Long taskAssigneeId);

}
