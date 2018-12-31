package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

import java.util.List;


public interface ActivityInstanceStorage {

    void insert(ActivityInstance activityInstance,
                ProcessEngineConfiguration processEngineConfiguration);

    ActivityInstance update(ActivityInstance activityInstance,
                            ProcessEngineConfiguration processEngineConfiguration);

    ActivityInstance find(String activityInstanceId,
                          ProcessEngineConfiguration processEngineConfiguration);

    void remove(String activityInstanceId,
                ProcessEngineConfiguration processEngineConfiguration);

    List<ActivityInstance> findAll(String processInstanceId,
                                   ProcessEngineConfiguration processEngineConfiguration);

}
