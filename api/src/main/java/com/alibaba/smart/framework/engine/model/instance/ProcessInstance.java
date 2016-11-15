package com.alibaba.smart.framework.engine.model.instance;

import java.util.List;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ProcessInstance extends LifeCycleInstance {

    String getProcessUri();

    void setProcessUri(String processUri);

    String getParentInstanceId();

    void setParentInstanceId(String parentInstanceId);

    String getParentExecutionInstanceId();

    void setParentExecutionInstanceId(String parentExecutionInstanceId);

    String getParentActivityInstanceId();

    void setParentActivityInstanceId(String parentActivityInstanceId);

    void addActivityInstance(ActivityInstance activityInstance);

    List<ActivityInstance> getActivityInstances();


}
