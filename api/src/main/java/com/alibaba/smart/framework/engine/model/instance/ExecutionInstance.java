package com.alibaba.smart.framework.engine.model.instance;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ExecutionInstance extends LifeCycleInstance {

    String getProcessDefinitionIdAndVersion();

    void setProcessDefinitionIdAndVersion(String processDefinitionIdAndVersion);


    String getProcessInstanceId();

    void setProcessInstanceId(String processInstanceId);

    boolean isActive();

    void setActive(boolean active);

    String getProcessDefinitionActivityId();

    void setProcessDefinitionActivityId(String processDefinitionActivityId);


    String getActivityInstanceId();

    void setActivityInstanceId(String activityInstanceId);


    void setTaskInstance(TaskInstance taskInstance);

    TaskInstance getTaskInstance();

    /**
     * 获取进入活动的关联
     *
     * @return 进入活动的关联
     */
    TransitionInstance getIncomeTransition();

    /**
     * 设置进入活动的关联
     *
     * @param transitionInstance 进入活动的关联
     */
    void setIncomeTransition(TransitionInstance transitionInstance);

    InstanceStatus getStatus();

    void setStatus(InstanceStatus status);
}
