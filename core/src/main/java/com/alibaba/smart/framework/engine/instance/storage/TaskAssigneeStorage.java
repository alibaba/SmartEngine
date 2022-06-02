package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;

public interface TaskAssigneeStorage {

    List<TaskAssigneeInstance> findList(String taskInstanceId,
                                        ProcessEngineConfiguration processEngineConfiguration);

    Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList,
                                                                       ProcessEngineConfiguration processEngineConfiguration) ;


    List<TaskAssigneeInstance> findPendingTaskAssigneeList(PendingTaskQueryParam pendingTaskQueryParam,
                                                           ProcessEngineConfiguration processEngineConfiguration);

    Long countPendingTaskAssigneeList(PendingTaskQueryParam pendingTaskQueryParam,
                                      ProcessEngineConfiguration processEngineConfiguration);

    TaskAssigneeInstance insert(TaskAssigneeInstance taskAssigneeInstance,
                                ProcessEngineConfiguration processEngineConfiguration);

    TaskAssigneeInstance update(String taskAssigneeInstanceId, String assigneeId,
                                ProcessEngineConfiguration processEngineConfiguration);

    TaskAssigneeInstance findOne(String taskAssigneeId,
                                 ProcessEngineConfiguration processEngineConfiguration);

    void remove(String taskAssigneeInstanceId,
                ProcessEngineConfiguration processEngineConfiguration);

    void removeAll(String taskInstanceId,
                   ProcessEngineConfiguration processEngineConfiguration);

}
