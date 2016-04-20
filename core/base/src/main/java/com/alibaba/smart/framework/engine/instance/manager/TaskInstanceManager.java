package com.alibaba.smart.framework.engine.instance.manager;

import com.alibaba.smart.framework.engine.instance.TaskInstance;

/**
 * Created by ettear on 16-4-18.
 */
public interface TaskInstanceManager {
    TaskInstance create(TaskInstance taskInstance);
    TaskInstance complete(String processInstanceId,String taskInstanceId);
}
