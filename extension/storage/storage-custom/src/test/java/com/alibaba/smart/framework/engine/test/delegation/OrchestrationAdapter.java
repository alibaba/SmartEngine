package com.alibaba.smart.framework.engine.test.delegation;

import java.util.Map;

public interface OrchestrationAdapter {

     void execute(Map<String, Object> request, Map<String, Object> response);

}