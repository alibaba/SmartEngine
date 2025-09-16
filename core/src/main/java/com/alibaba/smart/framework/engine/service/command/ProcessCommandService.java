package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import java.util.Map;

/**
 * 流程实例管理服务，包括流程实例启动，终止等等。
 *
 * @author 高海军 帝奇 2016.11.11
 * @author ettear 2016.04.13
 */
public interface ProcessCommandService {
    ProcessInstance start(
            String processDefinitionId,
            String processDefinitionVersion,
            Map<String, Object> request,
            Map<String, Object> response);

    ProcessInstance start(
            String processDefinitionId,
            String processDefinitionVersion,
            Map<String, Object> request);

    ProcessInstance start(String processDefinitionId, String processDefinitionVersion);

    ProcessInstance start(
            String processDefinitionId, String processDefinitionVersion, String tenantId);

    ProcessInstance startWith(
            String deploymentInstanceId,
            String userId,
            Map<String, Object> request,
            Map<String, Object> response);

    ProcessInstance startWith(
            String deploymentInstanceId, String userId, Map<String, Object> request);

    ProcessInstance startWith(String deploymentInstanceId, Map<String, Object> request);

    ProcessInstance startWith(String deploymentInstanceId);

    ProcessInstance startWith(String deploymentInstanceId, String tenantId);

    /**
     * 将流程实例,其他活跃的执行实例,任务实例都 abort 掉. 并行网关下可能由其他活跃的实例,所以需要全局关闭.
     *
     * @param processInstanceId
     */
    void abort(String processInstanceId);

    void abort(String processInstanceId, String tenantId);

    void abort(String processInstanceId, String reason, String tenantId);

    void abort(String processInstanceId, Map<String, Object> request);
}
