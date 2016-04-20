package com.alibaba.smart.framework.engine.instance.store;

import com.alibaba.smart.framework.engine.instance.ActivityInstance;

/**
 * 活动实例存储
 * Created by ettear on 16-4-19.
 */
public interface ActivityInstanceStorage {
    /**
     * 保存活动实例
     * @param instance 实例
     */
    ActivityInstance save(ActivityInstance instance);

    /**
     * 加载活动实例
     * @param processInstanceId 流程实例ID
     * @param activityInstanceId 活动实例ID
     * @return 实例
     */
    ActivityInstance find(String processInstanceId,String activityInstanceId);
    /**
     * 加载活动实例
     * @param processInstanceId 流程实例ID
     * @param activityInstanceId 活动实例ID
     * @return 实例
     */
    ActivityInstance findAll(String processInstanceId,String activityInstanceId);
    /**
     * 删除活动实例
     * @param processInstanceId 流程实例ID
     * @param activityInstanceId 活动实例ID
     */
    void remove(String processInstanceId,String activityInstanceId);
}
