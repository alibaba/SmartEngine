package com.alibaba.smart.framework.engine.model;

/**
 * 任务实例 Created by ettear on 16-4-13.
 */
public interface TaskInstance extends LifeCycleInstance {

    String getName();

    void setName(String name);

    String getProcessInstanceId();

    void setProcessInstanceId(String processInstanceId);

    String getExecutionInstanceId();

    void setExecutionInstanceId(String executionInstanceId);

    String getActivityInstanceId();

    void setActivityInstanceId(String activityInstanceId);
}
