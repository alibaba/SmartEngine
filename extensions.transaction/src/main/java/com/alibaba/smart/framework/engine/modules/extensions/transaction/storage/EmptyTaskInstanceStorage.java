package com.alibaba.smart.framework.engine.modules.extensions.transaction.storage;

import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

import java.util.List;

/**
 * @author Leo.yy   Created on 2017/8/3.
 * @description
 * @see
 */
public class EmptyTaskInstanceStorage implements TaskInstanceStorage {

    @Override
    public List<TaskInstance> findTaskByProcessInstanceIdAndStatus(TaskInstanceQueryParam taskInstanceQueryParam) {
        return null;
    }

    @Override
    public List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam) {
        return null;
    }

    @Override
    public Integer countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam) {
        return null;
    }

    @Override
    public List<TaskInstance> findTaskList(TaskInstanceQueryParam taskInstanceQueryParam) {
        return null;
    }

    @Override
    public Integer count(TaskInstanceQueryParam taskInstanceQueryParam) {
        return null;
    }

    @Override
    public TaskInstance insert(TaskInstance taskInstance) {
        return null;
    }

    @Override
    public TaskInstance update(TaskInstance taskInstance) {
        return null;
    }

    @Override
    public int updateFromStatus(TaskInstance taskInstance, String fromStatus) {
        return 0;
    }

    @Override
    public TaskInstance find(Long taskInstanceId) {
        return null;
    }

    @Override
    public void remove(Long taskInstanceId) {

    }
}
