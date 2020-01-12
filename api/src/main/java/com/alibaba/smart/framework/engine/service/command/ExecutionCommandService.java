package com.alibaba.smart.framework.engine.service.command;

import java.util.Map;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * 驱动引擎流转服务。 该服务区别于 TaskCommandService，主要负责类似  UserTask 这样的节点。
 *
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ExecutionCommandService {

    ProcessInstance signal(String executionInstanceId, Map<String, Object> request, Map<String, Object> response);


    ProcessInstance signal(String executionInstanceId, Map<String, Object> request);

    ProcessInstance signal(String executionInstanceId);

    /**
     * 驱动流程从指定环节重新开始执行
     *
     * @param executionInstanceId
     * @param activityId
     * @param request
     * @return
     */
    ProcessInstance jumpFrom(String executionInstanceId, String activityId, Map<String, Object> request);

    /**
     * 驱动流程到指定环节
     *
     * @param executionInstanceId
     * @param activityId
     * @param request
     * @return
     */
    ProcessInstance jumpTo(String executionInstanceId, String activityId, Map<String, Object> request);


    void retry(String processInstanceId, String activityId, ExecutionContext executionContext);

}
