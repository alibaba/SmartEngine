package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import java.util.Map;

/**
 * 驱动引擎流转服务。 该服务区别于 TaskCommandService。
 *
 * @author 高海军 帝奇 2016.11.11
 * @author ettear 2016.04.13
 */
public interface ExecutionCommandService {

    ProcessInstance signal(
            String executionInstanceId, Map<String, Object> request, Map<String, Object> response);

    ProcessInstance signal(
            String processInstanceId,
            String executionInstanceId,
            Map<String, Object> request,
            Map<String, Object> response);

    ProcessInstance signal(String executionInstanceId, Map<String, Object> request);

    ProcessInstance signal(String executionInstanceId);

    /** UNSAFE!!! */
    void markDone(String executionInstanceId);

    void markDone(String executionInstanceId, String tenantId);

    /**
     * UNSAFE!!!
     *
     * <p>Signal processInstance from the specific activity
     *
     * <p>executionInstanceId，request may be null
     */
    ProcessInstance jumpFrom(
            ProcessInstance processInstance,
            String activityId,
            String executionInstanceId,
            Map<String, Object> request);

    /**
     * UNSAFE!!!
     *
     * <p>Just jump to the specific activity only.
     *
     * <p>executionInstanceId，request may be null
     */
    ProcessInstance jumpTo(
            String processInstanceId,
            String processDefinitionId,
            String version,
            InstanceStatus instanceStatus,
            String processDefinitionActivityId,
            String tenantId);

    /**
     * UNSAFE!!!
     *
     * <p>Just execute the delegation of the activity only, not signal process instance.
     */
    void retry(
            ProcessInstance processInstance, String activityId, ExecutionContext executionContext);

    /**
     * UNSAFE!!!
     *
     * <p>create new executionInstance for specific activity only.
     *
     * <p>instance status is active default.
     */
    ExecutionInstance createExecution(ActivityInstance activityInstance);
}
