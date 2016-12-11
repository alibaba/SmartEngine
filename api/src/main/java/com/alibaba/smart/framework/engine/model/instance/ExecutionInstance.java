package com.alibaba.smart.framework.engine.model.instance;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ExecutionInstance extends LifeCycleInstance {

    String getProcessDefinitionIdAndVersion();

    void setProcessDefinitionIdAndVersion(String processDefinitionIdAndVersion);


    Long getProcessInstanceId();

    void setProcessInstanceId(Long processInstanceId);

    boolean isActive();

    void setActive(boolean active);

    String getActivityId();

    void setActivityId(String activityId);


    Long getActivityInstanceId();

    void setActivityInstanceId(Long activityInstanceId);


    //TUNE 这种对象关联需要在service处理,感觉有点不太好.
    void setTaskInstance(TaskInstance taskInstance);

    TaskInstance getTaskInstance();


}
