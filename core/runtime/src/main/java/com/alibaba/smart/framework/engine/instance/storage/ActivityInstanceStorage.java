package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;


public interface ActivityInstanceStorage {

    ActivityInstance save(ActivityInstance activityInstance);

    ActivityInstance find(String activityInstanceId);

    void remove(String activityInstanceId);

}
