package com.alibaba.smart.framework.engine.instance;

import java.util.List;

/**
 * Created by ettear on 16-4-12.
 */
public interface ProcessInstance {
    String getId();
    List<ExecutionInstance> getExecutions();
}
