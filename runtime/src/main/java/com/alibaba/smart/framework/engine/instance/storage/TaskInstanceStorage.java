package com.alibaba.smart.framework.engine.instance.storage;

import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

import java.util.List;


public interface TaskInstanceStorage {

    List<TaskInstance> findTaskList(TaskInstanceQueryParam taskInstanceQueryParam);

    //List<TaskInstance> findTask(Long processInstanceId,Long activityInstanceId);


    TaskInstance insert(TaskInstance taskInstance);

    TaskInstance update(TaskInstance taskInstance);

    TaskInstance find(Long taskInstanceId);

    void remove(Long taskInstanceId);

}
