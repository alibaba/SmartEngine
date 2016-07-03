package com.alibaba.smart.framework.engine.model.instance;

import java.util.Map;

/**
 * 流程实例 Created by ettear on 16-4-12.
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

//    InstanceFact getFact();
//
//    void setFact(InstanceFact fact);

    /**
     * 获取流程的执行实例
     * 
     * @return 流程执行实例
     */
    Map<String, ExecutionInstance> getExecutions();

    void addExecution(ExecutionInstance executionInstance);

    void removeExecution(String executionInstanceId);

    void addActivityInstance(ActivityInstance activityInstance);

}
