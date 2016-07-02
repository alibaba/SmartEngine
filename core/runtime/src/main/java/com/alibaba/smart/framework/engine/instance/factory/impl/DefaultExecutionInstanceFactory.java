package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.alibaba.smart.framework.engine.instance.util.InstanceIdUtil;
import com.alibaba.smart.framework.engine.model.ExecutionInstance;

/**
 * 默认执行实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultExecutionInstanceFactory implements ExecutionInstanceFactory {

    @Override
    public ExecutionInstance create() {
        DefaultExecutionInstance defaultExecutionInstance = new DefaultExecutionInstance();
        defaultExecutionInstance.setInstanceId(InstanceIdUtil.uuid());
        return defaultExecutionInstance;
    }
}
