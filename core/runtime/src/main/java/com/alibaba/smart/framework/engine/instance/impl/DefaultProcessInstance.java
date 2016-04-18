package com.alibaba.smart.framework.engine.instance.impl;

import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;

import java.util.List;

/**
 * Created by ettear on 16-4-12.
 */
public class DefaultProcessInstance implements ProcessInstance {

    @Override
    public String getId() {
        return null;
    }

    @Override
    public List<ExecutionInstance> getRunningExecutions() {
        return null;
    }

    @Override
    public List<ExecutionInstance> getExecutions() {
        return null;
    }
}
