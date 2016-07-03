package com.alibaba.smart.framework.engine.service;

import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
 
public interface ExecutionService {


    ProcessInstance signal(String processInstanceId, String executionInstanceId, Map<String, Object> variables);
}
