package com.alibaba.smart.framework.engine.instance;

/**
 * Created by ettear on 16-4-18.
 */
public interface TaskManager {
    TaskInstance create(String activityInstanceId, TaskInstance task);
}
