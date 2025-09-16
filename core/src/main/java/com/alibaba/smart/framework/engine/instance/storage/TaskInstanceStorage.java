package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

import java.util.List;

public interface TaskInstanceStorage {

    List<TaskInstance> findTaskByProcessInstanceIdAndStatus(
            TaskInstanceQueryParam taskInstanceQueryParam,
            ProcessEngineConfiguration processEngineConfiguration);

    List<TaskInstance> findPendingTaskList(
            PendingTaskQueryParam pendingTaskQueryParam,
            ProcessEngineConfiguration processEngineConfiguration);

    Long countPendingTaskList(
            PendingTaskQueryParam pendingTaskQueryParam,
            ProcessEngineConfiguration processEngineConfiguration);

    List<TaskInstance> findTaskListByAssignee(
            TaskInstanceQueryByAssigneeParam param,
            ProcessEngineConfiguration processEngineConfiguration);

    Long countTaskListByAssignee(
            TaskInstanceQueryByAssigneeParam param,
            ProcessEngineConfiguration processEngineConfiguration);

    List<TaskInstance> findTaskList(
            TaskInstanceQueryParam taskInstanceQueryParam,
            ProcessEngineConfiguration processEngineConfiguration);

    Long count(
            TaskInstanceQueryParam taskInstanceQueryParam,
            ProcessEngineConfiguration processEngineConfiguration);

    TaskInstance insert(
            TaskInstance taskInstance, ProcessEngineConfiguration processEngineConfiguration);

    TaskInstance update(
            TaskInstance taskInstance, ProcessEngineConfiguration processEngineConfiguration);

    int updateFromStatus(
            TaskInstance taskInstance,
            String fromStatus,
            ProcessEngineConfiguration processEngineConfiguration);

    TaskInstance find(
            String taskInstanceId,
            String tenantId,
            ProcessEngineConfiguration processEngineConfiguration);

    void remove(
            String taskInstanceId,
            String tenantId,
            ProcessEngineConfiguration processEngineConfiguration);
}
