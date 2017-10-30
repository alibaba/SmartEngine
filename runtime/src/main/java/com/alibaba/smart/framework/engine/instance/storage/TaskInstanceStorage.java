package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;


public interface TaskInstanceStorage {

    List<TaskInstance> findTaskByProcessInstanceIdAndStatus(TaskInstanceQueryParam taskInstanceQueryParam);

    List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam);

    Integer countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam);

    List<TaskInstance> findTaskList(TaskInstanceQueryParam taskInstanceQueryParam);

    Integer count(TaskInstanceQueryParam taskInstanceQueryParam);

    TaskInstance insert(TaskInstance taskInstance);

    TaskInstance update(TaskInstance taskInstance);

    int updateFromStatus(TaskInstance taskInstance,String fromStatus);

    TaskInstance find(Long taskInstanceId);

    void remove(Long taskInstanceId);

}
