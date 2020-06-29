package com.alibaba.smart.framework.engine.service.command;

import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * 驱动引擎流转服务。 该服务区别于 TaskCommandService。
 *
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ExecutionCommandService {

    ProcessInstance signal(String executionInstanceId, Map<String, Object> request, Map<String, Object> response);


    ProcessInstance signal(String executionInstanceId, Map<String, Object> request);

    ProcessInstance signal(String executionInstanceId);

    /**
     * UNSAFE!!!
     *
     */
    void markDone(String executionInstanceId);

    /**
     * UNSAFE!!!
     *
     * Signal processInstance from the specific activity
     *
     * executionInstanceId，request may be null
     */
    ProcessInstance jumpFrom(ProcessInstance processInstance,String activityId,String executionInstanceId, Map<String, Object> request);

    /**
     * UNSAFE!!!
     *
     * Just jump to the specific activity only.
     *
     * executionInstanceId，request may be null
     */
    ProcessInstance jumpTo(String processInstanceId, String  processDefinitionId, String version,
                           InstanceStatus instanceStatus, String processDefinitionActivityId);

    /**
     * UNSAFE!!!
     *
     * Just execute the delegation of the activity only, not signal process instance.
     *
     */
    void retry(ProcessInstance processInstance, String activityId, ExecutionContext executionContext);
    
    /**
     * UNSAFE!!!
     *
     * create new executionInstance for specific activity only.
     *
     * instance status is active default.
     */
    ExecutionInstance createExecution(ActivityInstance activityInstance);

}
