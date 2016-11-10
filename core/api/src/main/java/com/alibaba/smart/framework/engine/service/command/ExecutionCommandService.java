package com.alibaba.smart.framework.engine.service.command;

import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
 
public interface ExecutionCommandService {


    ProcessInstance signal(String processInstanceId, String executionInstanceId, Map<String, Object> variables);
}
