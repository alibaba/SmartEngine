package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

import java.util.List;


public interface TaskInstanceStorage {

     List<TaskInstance> findPendingTask(Long processInstanceId);

    TaskInstance save(TaskInstance taskInstance);

    TaskInstance find(Long taskInstanceId);

    void remove(Long taskInstanceId);

}
