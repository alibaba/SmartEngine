package com.alibaba.smart.framework.engine.model.instance;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface TaskInstance extends LifeCycleInstance {

    String getActivityId();

    void setActivityId(String activityId);

    Long getProcessInstanceId();

    void setProcessInstanceId(Long processInstanceId);

    Long getExecutionInstanceId();

    void setExecutionInstanceId(Long executionInstanceId);

    Long getActivityInstanceId();

    void setActivityInstanceId(Long activityInstanceId);
}
