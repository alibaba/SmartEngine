package com.alibaba.smart.framework.engine.service.impl;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.TaskService;

import java.util.List;
import java.util.Map;

/**
 * Created by ettear on 16-4-19.
 */
public class DefaultTaskService implements TaskService {

    @Override
    public void complete(  String taskId, Map<String, Object> variables) {

    }

    @Override
    public List<TaskInstance> find(String processInstanceId) {
        // TODO Auto-generated method stub
        return null;
    }
}