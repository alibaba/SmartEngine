package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.instance.factory.ActivityInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.alibaba.smart.framework.engine.instance.utils.InstanceIdUtils;

/**
 * 默认活动实例工厂实现
 * Created by ettear on 16-4-20.
 */
public class DefaultActivityInstanceFactory implements ActivityInstanceFactory {

    @Override
    public ActivityInstance create() {
        DefaultActivityInstance defaultActivityInstance = new DefaultActivityInstance();
        defaultActivityInstance.setInstanceId(InstanceIdUtils.uuid());
        return defaultActivityInstance;
    }
}
