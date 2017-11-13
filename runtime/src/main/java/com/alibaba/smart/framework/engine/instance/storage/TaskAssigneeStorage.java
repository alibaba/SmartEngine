package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;


public interface TaskAssigneeStorage {

    List<TaskAssigneeInstance> findList(Long  taskInstanceId);

    TaskAssigneeInstance insert( TaskAssigneeInstance taskAssigneeInstance);

    TaskAssigneeInstance update(Long taskAssigneeId,String assigneeId);

    TaskAssigneeInstance findOne(Long taskAssigneeId);

    void remove(Long taskAssigneeId);

}
