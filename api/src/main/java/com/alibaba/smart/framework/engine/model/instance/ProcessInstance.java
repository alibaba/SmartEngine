package com.alibaba.smart.framework.engine.model.instance;

import java.util.List;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ProcessInstance extends LifeCycleInstance {

    /**
     * get ProcessDefinitionId and ProcessDefinitionVersion
     * @return ProcessDefinitionId and ProcessDefinitionVersion
     */
    String getProcessDefinitionIdAndVersion();

    /**
     * get ProcessDefinitionId
     * @return ProcessDefinitionId
     */
    String getProcessDefinitionId();

    void setProcessDefinitionId(String processDefinitionId);

    void setProcessDefinitionVersion(String processDefinitionVersion);

    /**
     * get ProcessDefinitionVersion
     * @return ProcessDefinitionVersion
     */
    String getProcessDefinitionVersion();

    void setProcessDefinitionIdAndVersion(String processDefinitionIdAndVersion);

    String getProcessDefinitionType();

    void setProcessDefinitionType(String processDefinitionType);

    String getStartUserId();

    void setStartUserId(String startUserId);

    Long getParentInstanceId();

    void setParentInstanceId(Long parentInstanceId);


    Long getParentExecutionInstanceId();

    void setParentExecutionInstanceId(Long parentExecutionInstanceId);

    InstanceStatus getStatus();

    void setStatus(InstanceStatus status);

    boolean isSuspend();

    void setSuspend(boolean suspend);

    // should used  internally
    void addActivityInstance(ActivityInstance activityInstance);

    List<ActivityInstance> getActivityInstances();


    String getBizUniqueId();

    void setBizUniqueId(String bizUniqueId);

    String getReason();

    void setReason(String reason);

    String getTag();

    void setTag(String tag);

    String getTitle();

    void setTitle(String title);

    String getComment();

    void setComment(String comment);
}
