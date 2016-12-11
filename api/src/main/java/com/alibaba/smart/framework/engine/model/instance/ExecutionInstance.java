package com.alibaba.smart.framework.engine.model.instance;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ExecutionInstance extends LifeCycleInstance {

    Long getProcessInstanceId();

    void setProcessInstanceId(Long processInstanceId);

    String getActivityId();

    void setActivityId(String activityId);


    Long getActivityInstanceId();

    void setActivityInstanceId(Long activityInstanceId);


    void setTaskInstance(TaskInstance taskInstance);

    TaskInstance getTaskInstance();


}
