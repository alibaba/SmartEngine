package com.alibaba.smart.framework.engine.model.instance;

/**
 * @author 高海军 帝奇 2016.11.11
 * @author ettear 2016.04.13
 */
public interface TaskAssigneeInstance extends LifeCycleInstance {

    String getProcessInstanceId();

    void setProcessInstanceId(String processInstanceId);

    String getTaskInstanceId();

    void setTaskInstanceId(String taskInstanceId);

    String getAssigneeId();

    void setAssigneeId(String assigneeId);

    String getAssigneeType();

    void setAssigneeType(String assigneeType);
}
