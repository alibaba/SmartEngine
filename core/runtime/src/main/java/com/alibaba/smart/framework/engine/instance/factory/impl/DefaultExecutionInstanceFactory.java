package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.instance.util.InstanceIdUtil;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.param.ExecutionParam;

/**
 * 默认执行实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultExecutionInstanceFactory implements ExecutionInstanceFactory {



    @Override
    public ExecutionInstance create() {
        DefaultExecutionInstance defaultExecutionInstance = new DefaultExecutionInstance();
        defaultExecutionInstance.setInstanceId(InstanceIdUtil.simpleId());
        return defaultExecutionInstance;
    }

    @Override
    public ExecutionInstance recovery(ExecutionParam executionParam) {
        DefaultExecutionInstance executionInstance = new DefaultExecutionInstance();
        executionInstance.getModle(executionParam);
        return executionInstance;
    }

    @Override
    public String toDatabase(ExecutionInstance executionInstance) {
        if (executionInstance instanceof DefaultExecutionInstance) {
            return ((DefaultExecutionInstance) executionInstance).toDatabase();
        }
        return null;
    }
}
