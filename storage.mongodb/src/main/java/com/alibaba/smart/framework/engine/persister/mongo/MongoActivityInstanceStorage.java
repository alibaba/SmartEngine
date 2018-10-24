package com.alibaba.smart.framework.engine.persister.mongo;

import java.util.List;

import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

/**
 * Created by 高海军 帝奇 74394 on 2018 October  21:52.
 */
public class MongoActivityInstanceStorage implements ActivityInstanceStorage {

    @Override
    public ActivityInstance insert(ActivityInstance activityInstance) {
        return null;
    }

    @Override
    public ActivityInstance update(ActivityInstance activityInstance) {
        return null;
    }

    @Override
    public ActivityInstance find(String activityInstanceId) {
        return null;
    }

    @Override
    public void remove(String activityInstanceId) {

    }

    @Override
    public List<ActivityInstance> findAll(String processInstanceId) {
        return null;
    }
}