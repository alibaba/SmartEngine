package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.instance.factory.TransitionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTransitionInstance;
import com.alibaba.smart.framework.engine.instance.util.InstanceIdUtil;
import com.alibaba.smart.framework.engine.model.TransitionInstance;

/**
 * 默认关联实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultTransitionInstanceFactory implements TransitionInstanceFactory {

    @Override
    public TransitionInstance create() {
        DefaultTransitionInstance defaultTransitionInstance = new DefaultTransitionInstance();
        defaultTransitionInstance.setInstanceId(InstanceIdUtil.uuid());
        return defaultTransitionInstance;
    }
}
