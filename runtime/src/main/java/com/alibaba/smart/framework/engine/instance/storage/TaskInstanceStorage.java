package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;

import java.util.List;


public interface TaskInstanceStorage {

    List<TaskInstance> findPendingTask(Long processInstanceId);

    List<TaskInstance> findTask(Long processInstanceId,Long activityInstanceId);


    TaskInstance insert(TaskInstance taskInstance);

    TaskInstance update(TaskInstance taskInstance);

    TaskInstance find(Long taskInstanceId);

    void remove(Long taskInstanceId);

}
