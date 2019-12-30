package com.alibaba.smart.framework.engine.service.command;

import java.util.Map;

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
     * 驱动流程转向自由节点
     *
     * @param executionInstanceId
     * @param activityId
     * @param request
     * @return
     */
    ProcessInstance jump(String executionInstanceId, String activityId, Map<String, Object> request);



}
