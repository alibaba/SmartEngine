package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;


public interface TaskAssigneeStorage {

    List<TaskAssigneeInstance> findList(String  taskInstanceId);

    Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList) ;

    TaskAssigneeInstance insert( TaskAssigneeInstance taskAssigneeInstance);

    TaskAssigneeInstance update(String taskAssigneeId,String assigneeId);

    TaskAssigneeInstance findOne(String taskAssigneeId);

    void remove(String taskAssigneeId);

}
