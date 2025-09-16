package com.alibaba.smart.framework.engine.model.instance;

/**
 * @author 高海军 帝奇 2016.11.11
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

    String getBlockId();

    void setBlockId(String blockId);

    void setTaskInstance(TaskInstance taskInstance);

    TaskInstance getTaskInstance();
}
