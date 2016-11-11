package com.alibaba.smart.framework.engine.model.instance;

import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ProcessInstance extends LifeCycleInstance {

    //TODO 干掉?
    String getProcessUri();

    void setProcessUri(String processUri);

    String getParentInstanceId();

    void setParentInstanceId(String parentInstanceId);

    String getParentExecutionInstanceId();

    void setParentExecutionInstanceId(String parentExecutionInstanceId);

    String getParentActivityInstanceId();

    void setParentActivityInstanceId(String parentActivityInstanceId);



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
