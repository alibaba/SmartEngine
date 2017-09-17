package com.alibaba.smart.framework.engine.model.instance;

import java.util.Date;
import java.util.List;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface TaskInstance extends LifeCycleInstance {

    String getProcessDefinitionIdAndVersion();

    void setProcessDefinitionIdAndVersion(String processDefinitionIdAndVersion);

    Long getProcessInstanceId();

    void setProcessInstanceId(Long processInstanceId);

    String getActivityId();

    void setActivityId(String activityId);

    Long getExecutionInstanceId();

    void setExecutionInstanceId(Long executionInstanceId);

    Long getActivityInstanceId();

    void setActivityInstanceId(Long activityInstanceId);

    String getClaimUserId();

    void setClaimUserId(String claimUserId);

    String getStatus();

    void setStatus(String status);

    List<TaskAssigneeInstance> getTaskAssigneeInstanceList();

    void setTaskAssigneeInstanceList(List<TaskAssigneeInstance> taskAssigneeInstanceList );

    Integer getPriority();

    void setPriority(Integer priority);

    Date getClaimTime();

    void setClaimTime(Date claimTime);

    //Date getEndTime();
    //
    //void setEndTime(Date endTime);

}
