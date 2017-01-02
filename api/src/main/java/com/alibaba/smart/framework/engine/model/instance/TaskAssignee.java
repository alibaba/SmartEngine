package com.alibaba.smart.framework.engine.model.instance;

import java.util.Date;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface TaskAssignee extends LifeCycleInstance {

    String getProcessDefinitionIdAndVersion();

    void setProcessDefinitionIdAndVersion(String processDefinitionIdAndVersion);


    String getActivityId();

    void setActivityId(String activityId);

    Long getProcessInstanceId();

    void setProcessInstanceId(Long processInstanceId);

    Long getExecutionInstanceId();

    void setExecutionInstanceId(Long executionInstanceId);

    Long getActivityInstanceId();

    void setActivityInstanceId(Long activityInstanceId);

    String getTitle() ;

    void setTitle(String title);

      String getAssigneeId() ;

      void setAssigneeId(String assigneeId);



      Integer getPriority();

      void setPriority(Integer priority) ;

      Date getClaimTime() ;

      void setClaimTime(Date claimTime) ;

      Date getEndTime() ;

      void setEndTime(Date endTime);

}
