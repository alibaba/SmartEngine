package com.alibaba.smart.framework.engine.persister.custom;

import java.util.List;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:54.
 */
public class CustomTaskInstanceStorage implements TaskInstanceStorage {

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
        throw new EngineException("not implement intentionally");
    }

    @Override
    public Integer count(TaskInstanceQueryParam taskInstanceQueryParam) {
        return null;
    }

    @Override
    public TaskInstance insert(TaskInstance instance) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public TaskInstance update(TaskInstance instance) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public int updateFromStatus(TaskInstance taskInstance, String fromStatus) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public TaskInstance find(Long instanceId) {
        throw new EngineException("not implement intentionally");
    }

    @Override
    public void remove(Long instanceId) {
        throw new EngineException("not implement intentionally");
    }
}
