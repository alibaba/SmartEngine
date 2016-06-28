package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.instance.utils.InstanceIdUtils;

/**
 * 默认执行实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultExecutionInstanceFactory implements ExecutionInstanceFactory {

    @Override
    public ExecutionInstance create() {
        DefaultExecutionInstance defaultExecutionInstance = new DefaultExecutionInstance();
        defaultExecutionInstance.setInstanceId(InstanceIdUtils.uuid());
        return defaultExecutionInstance;
    }
}
