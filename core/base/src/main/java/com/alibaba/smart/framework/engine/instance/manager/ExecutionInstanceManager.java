package com.alibaba.smart.framework.engine.instance.manager;

import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.ExecutionInstance;

import java.util.List;

/**
 * Created by ettear on 16-4-18.
 */
public interface ExecutionInstanceManager {

    ExecutionInstance create(ExecutionInstance execution);

    void updateActivity(String processInstanceId,String executionInstanceId,ActivityInstance activityInstance);

    void suspend(String processInstanceId,String executionInstanceId,String step);

    void complete(String processInstanceId,String executionInstanceId);

    List<ExecutionInstance> findExecutions(String processInstanceId);

}
