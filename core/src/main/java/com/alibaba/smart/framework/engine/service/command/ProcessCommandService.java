package com.alibaba.smart.framework.engine.service.command;

import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * 流程实例管理服务，不包括流程实例启动，终止等等。
 *
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */

public interface ProcessCommandService {
    ProcessInstance start(String processDefinitionId, String processDefinitionVersion, Map<String, Object> request, Map<String, Object> response);


    ProcessInstance start(String processDefinitionId, String processDefinitionVersion, Map<String, Object> request);

    ProcessInstance start(String processDefinitionId, String processDefinitionVersion);

    ProcessInstance startWith(String deploymentInstanceId, String userId, Map<String, Object> request, Map<String, Object> response);


    ProcessInstance startWith(String deploymentInstanceId, String userId, Map<String, Object> request);

    ProcessInstance startWith(String deploymentInstanceId, Map<String, Object> request);

    ProcessInstance startWith(String deploymentInstanceId);

    /**
     * 将流程实例,其他活跃的执行实例,任务实例都 abort 掉. 并行网关下可能由其他活跃的实例,所以需要全局关闭.
     *
     * @param processInstanceId
     */
    void abort(String processInstanceId);

    void abort(String processInstanceId, String reason);

    void abort(String processInstanceId, Map<String, Object> request);

}
