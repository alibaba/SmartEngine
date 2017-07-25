package com.alibaba.smart.framework.engine.model.instance;

import java.util.List;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ProcessInstance extends LifeCycleInstance {

    /**
     * get ProcessDefinitionId and ProcessDefinitionVersion
     * @return
     */
    String getProcessDefinitionIdAndVersion();

    /**
     * get ProcessDefinitionId
     * @return
     */
    String getProcessDefinitionId();

    /**
     * get ProcessDefinitionVersion
     * @return
     */
    String getProcessDefinitionVersion();

    void setProcessDefinitionIdAndVersion(String processDefinitionIdAndVersion);

    Long getParentInstanceId();

    void setParentInstanceId(Long parentInstanceId);


    Long getParentExecutionInstanceId();

    void setParentExecutionInstanceId(Long parentExecutionInstanceId);

    InstanceStatus getStatus();

    void setStatus(InstanceStatus status);

    boolean isSuspend();

    void setSuspend(boolean suspend);

//    Long getParentExecutionInstanceId();
//
//    void setParentExecutionInstanceId(Long parentExecutionInstanceId);
//
//    Long getParentActivityInstanceId();
//
//    void setParentActivityInstanceId(Long parentActivityInstanceId);

    // should used  internally
    void addNewActivityInstance(ActivityInstance activityInstance);

    List<ActivityInstance> getNewActivityInstances();


}
