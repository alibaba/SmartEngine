package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.instance.TaskInstance;
import com.alibaba.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.alibaba.smart.framework.engine.instance.utils.InstanceIdUtils;

/**
 * 默认任务实例工厂实现
 * Created by ettear on 16-4-20.
 */
public class DefaultTaskInstanceFactory implements TaskInstanceFactory {

    @Override
    public TaskInstance create() {
        DefaultTaskInstance defaultTaskInstance = new DefaultTaskInstance();
        defaultTaskInstance.setInstanceId(InstanceIdUtils.uuid());
        return defaultTaskInstance;
    }
}
