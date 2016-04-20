package com.alibaba.smart.framework.engine.instance.store;

import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.TaskInstance;

/**
 * 任务实例存储
 * Created by ettear on 16-4-19.
 */
public interface TaskInstanceStorage {
    /**
     * 保存任务实例
     * @param instance 实例
     */
    TaskInstance save(TaskInstance instance);

    /**
     * 加载任务实例
     * @param processInstanceId 流程实例ID
     * @param taskInstanceId 任务实例ID
     * @return 实例
     */
    ActivityInstance load(String processInstanceId,String taskInstanceId);

    /**
     * 删除任务实例
     * @param processInstanceId 流程实例ID
     * @param taskInstanceId 任务实例ID
     */
    void remove(String processInstanceId,String taskInstanceId);

    /**
     * 完成任务实例
     * @param processInstanceId 流程实例ID
     * @param taskInstanceId 任务实例ID
     */
    void complete(String processInstanceId,String taskInstanceId);
}
