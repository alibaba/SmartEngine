package com.alibaba.smart.framework.engine.modules.extensions.transaction.storage;

import com.alibaba.smart.framework.engine.instance.storage.ActivityInstanceStorage;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;

import java.util.List;

/**
 * @author Leo.yy   Created on 2017/8/3.
 * @description
 * @see
 */
public class EmptyActivityInstanceStorage implements ActivityInstanceStorage {
    @Override
    public ActivityInstance insert(ActivityInstance activityInstance) {
        return null;
    }

    @Override
    public ActivityInstance update(ActivityInstance activityInstance) {
        return null;
    }

    @Override
    public ActivityInstance find(Long activityInstanceId) {
        return null;
    }

    @Override
    public void remove(Long activityInstanceId) {

    }

    @Override
    public List<ActivityInstance> findAll(Long processInstanceId) {
        return null;
    }
}
