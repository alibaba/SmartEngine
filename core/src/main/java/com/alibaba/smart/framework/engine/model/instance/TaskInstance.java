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

    String getProcessInstanceId();

    void setProcessInstanceId(String processInstanceId);

    String getProcessDefinitionActivityId();

    void setProcessDefinitionActivityId(String processDefinitionActivityId);

    String getProcessDefinitionType();

    void setProcessDefinitionType(String processDefinitionType);

    String getExecutionInstanceId();

    void setExecutionInstanceId(String executionInstanceId);

    String getActivityInstanceId();

    void setActivityInstanceId(String activityInstanceId);

    String getClaimUserId();

    void setClaimUserId(String claimUserId);

    String getStatus();


    /**
     * @see com.alibaba.smart.framework.engine.constant.TaskInstanceConstant
     * @param status
     */
    void setStatus(String status);

    String getTag();

    void setTag(String tag);

    List<TaskAssigneeInstance> getTaskAssigneeInstanceList();

    void setTaskAssigneeInstanceList(List<TaskAssigneeInstance> taskAssigneeInstanceList);

    Integer getPriority();

    void setPriority(Integer priority);

    Date getClaimTime();

    void setClaimTime(Date claimTime);

    //Date getEndTime();
    //
    //void setEndTime(Date endTime);

    String getComment();

    void setComment(String comment);

    String getExtension();

    void setExtension(String extension);


    String getTitle();

    void setTitle(String title);

}
