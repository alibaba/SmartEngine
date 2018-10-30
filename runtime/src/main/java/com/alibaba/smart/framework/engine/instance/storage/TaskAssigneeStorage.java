package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;


public interface TaskAssigneeStorage {

    List<TaskAssigneeInstance> findList(String taskInstanceId,
                                        ProcessEngineConfiguration processEngineConfiguration);

    Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList,
                                                                       ProcessEngineConfiguration processEngineConfiguration) ;

    TaskAssigneeInstance insert(TaskAssigneeInstance taskAssigneeInstance,
                                ProcessEngineConfiguration processEngineConfiguration);

    TaskAssigneeInstance update(String taskAssigneeId, String assigneeId,
                                ProcessEngineConfiguration processEngineConfiguration);

    TaskAssigneeInstance findOne(String taskAssigneeId,
                                 ProcessEngineConfiguration processEngineConfiguration);

    void remove(String taskAssigneeId,
                ProcessEngineConfiguration processEngineConfiguration);

}
