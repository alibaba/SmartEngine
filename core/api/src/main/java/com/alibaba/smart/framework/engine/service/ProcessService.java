package com.alibaba.smart.framework.engine.service;

import java.util.Map;

import com.alibaba.smart.framework.engine.instance.ProcessInstance;

/**
 * Created by ettear on 16-4-12.
 */
public interface ProcessService {

    ProcessInstance start(String processId, String version, Map<String, Object> variables);

    void abort(String processInstanceId);

    ProcessInstance find(String processInstanceId);
}
