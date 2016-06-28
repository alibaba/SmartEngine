package com.alibaba.smart.framework.engine.instance.manager;

import java.util.Map;

/**
 * Created by ettear on 16-4-19.
 */
public interface MessageManager {

    void signal(String processInstanceId, String executionId, Map<String, Object> variables);
}
