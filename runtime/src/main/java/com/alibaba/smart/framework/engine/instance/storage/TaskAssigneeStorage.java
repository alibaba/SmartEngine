package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;


public interface TaskAssigneeStorage {

    List<TaskAssigneeInstance> findList(Long  taskInstanceId);

    Map<Long, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<Long> taskInstanceIdList);

    TaskAssigneeInstance insert( TaskAssigneeInstance taskAssigneeInstance);

    TaskAssigneeInstance update(Long taskAssigneeId,String assigneeId);

    TaskAssigneeInstance findOne(Long taskAssigneeId);

    void remove(Long taskAssigneeId);

}
