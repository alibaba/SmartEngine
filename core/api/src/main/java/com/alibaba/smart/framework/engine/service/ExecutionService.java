package com.alibaba.smart.framework.engine.service;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import java.util.Map;
 
public interface ExecutionService {


    ProcessInstance signal(String processInstanceId, String executionInstanceId, Map<String, Object> variables);
}
