package com.alibaba.smart.framework.engine.service;

import java.util.Map;

import com.alibaba.smart.framework.engine.instance.ProcessInstance;

/**
 * Created by ettear on 16-4-19.
 */
public interface ExecutionService {


    ProcessInstance signal(String processInstanceId, String executionInstanceId, Map<String, Object> variables);
}
