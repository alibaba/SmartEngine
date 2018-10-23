package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

import java.util.List;


public interface ActivityInstanceStorage {

    ActivityInstance insert(ActivityInstance activityInstance);

    ActivityInstance update(ActivityInstance activityInstance);

    ActivityInstance find(String activityInstanceId);

    void remove(String activityInstanceId);

    List<ActivityInstance> findAll(String processInstanceId);

}
