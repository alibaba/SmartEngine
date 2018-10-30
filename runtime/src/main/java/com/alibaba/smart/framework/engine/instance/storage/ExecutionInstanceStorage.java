package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;

import java.util.List;


public interface ExecutionInstanceStorage {

    ExecutionInstance insert(ExecutionInstance executionInstance,
                             ProcessEngineConfiguration processEngineConfiguration);

    ExecutionInstance update(ExecutionInstance executionInstance,
                             ProcessEngineConfiguration processEngineConfiguration);


    ExecutionInstance find(String executionInstanceId,
                           ProcessEngineConfiguration processEngineConfiguration);

    void remove(String executionInstanceId,
                ProcessEngineConfiguration processEngineConfiguration);

    List<ExecutionInstance> findActiveExecution(String processInstanceId,
                                                ProcessEngineConfiguration processEngineConfiguration);

    List<ExecutionInstance> findByActivityInstanceId(String processInstanceId, String activityInstanceId,
                                                     ProcessEngineConfiguration processEngineConfiguration);

}
