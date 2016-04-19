package com.alibaba.smart.framework.engine.instance.manager;

import com.alibaba.smart.framework.engine.instance.ProcessInstance;

import java.util.Map;

/**
 * Created by ettear on 16-4-12.
 */
public interface ProcessManager {

    ProcessInstance start(String processId, String version, Map<String, Object> variables);

    void abort(String processInstanceId);

    ProcessInstance find(String processInstanceId);
}
