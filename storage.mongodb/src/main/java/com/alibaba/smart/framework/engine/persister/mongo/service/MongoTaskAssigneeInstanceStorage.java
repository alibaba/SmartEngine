package com.alibaba.smart.framework.engine.persister.mongo.service;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.alibaba.smart.framework.engine.model.instance.TaskAssigneeInstance;

/**
 * Created by 高海军 帝奇 74394 on 2018 October  21:52.
 */
public class MongoTaskAssigneeInstanceStorage implements TaskAssigneeStorage {

    private static final String INSTANCE = "se_task_instance";

    @Override
    public List<TaskAssigneeInstance> findList(String taskInstanceId,
                                               ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public Map<String, List<TaskAssigneeInstance>> findAssigneeOfInstanceList(List<String> taskInstanceIdList,
                                                                              ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public TaskAssigneeInstance insert(TaskAssigneeInstance taskAssigneeInstance,
                                       ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public TaskAssigneeInstance update(String taskAssigneeId, String assigneeId,
                                       ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public TaskAssigneeInstance findOne(String taskAssigneeId, ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public void remove(String taskAssigneeId, ProcessEngineConfiguration processEngineConfiguration) {

    }
}