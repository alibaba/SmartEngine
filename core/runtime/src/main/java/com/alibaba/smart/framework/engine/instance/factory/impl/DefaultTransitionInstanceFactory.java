package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.instance.TransitionInstance;
import com.alibaba.smart.framework.engine.instance.factory.TransitionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTransitionInstance;

/**
 * 默认关联实例工厂实现
 * Created by ettear on 16-4-20.
 */
public class DefaultTransitionInstanceFactory implements TransitionInstanceFactory{

    @Override
    public TransitionInstance create() {
        return new DefaultTransitionInstance();
    }
}
