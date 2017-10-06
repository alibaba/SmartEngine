package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import java.util.Map;

/**
 * 驱动引擎流转服务。 该服务区别于 TaskCommandService，主要负责类似  UserTask 这样的节点。
 *
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ExecutionCommandService {

    ProcessInstance signal(Long executionInstanceId, Map<String, Object> request);

    ProcessInstance signal(Long executionInstanceId);
}
