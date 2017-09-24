package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import java.util.Map;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */

public interface ProcessCommandService {


    ProcessInstance start(String processId, String version, Map<String, Object> request);

    ProcessInstance start(String processId, String version);

    ProcessInstance start(Long deploymentInstanceId, Map<String, Object> request);

    ProcessInstance start(Long deploymentInstanceId);


    void abort(Long processInstanceId);

    void abort(Long processInstanceId,String reason);


}
