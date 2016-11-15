package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;


public interface TaskInstanceStorage {

    TaskInstance save(TaskInstance taskInstance);

    TaskInstance find(String taskInstanceId);

    void remove(String taskInstanceId);

}
