package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import java.util.Map;

/**
 * 流程实例管理服务，不包括流程实例启动，终止等等。
 *
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */

public interface ProcessCommandService {


    ProcessInstance start(String processDefinitionId, String processDefinitionVersion, Map<String, Object> request);

    ProcessInstance start(String processDefinitionId, String processDefinitionVersion);

    ProcessInstance start(Long deploymentInstanceId, Map<String, Object> request);

    ProcessInstance start(Long deploymentInstanceId);

    void abort(Long processInstanceId);

    void abort(Long processInstanceId,String reason);


}
