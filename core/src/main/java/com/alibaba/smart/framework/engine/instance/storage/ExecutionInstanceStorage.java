package com.alibaba.smart.framework.engine.instance.storage;

import java.util.List;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

public interface ExecutionInstanceStorage {

    void insert(ExecutionInstance executionInstance,
                ProcessEngineConfiguration processEngineConfiguration);

    void update(ExecutionInstance executionInstance,
                ProcessEngineConfiguration processEngineConfiguration);


    ExecutionInstance find(String executionInstanceId,
                           ProcessEngineConfiguration processEngineConfiguration);

    ExecutionInstance findWithShading(String processInstanceId, String executionInstanceId,
            ProcessEngineConfiguration processEngineConfiguration);

    void remove(String executionInstanceId,
                ProcessEngineConfiguration processEngineConfiguration);

    List<ExecutionInstance> findActiveExecution(String processInstanceId,
                                                ProcessEngineConfiguration processEngineConfiguration);

    List<ExecutionInstance> findByActivityInstanceId(String processInstanceId, String activityInstanceId,
                                                     ProcessEngineConfiguration processEngineConfiguration);

    List<ExecutionInstance> findAll(String processInstanceId,
                                   ProcessEngineConfiguration processEngineConfiguration);
}
