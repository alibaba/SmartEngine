package com.alibaba.smart.framework.engine.configuration.impl;

import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.util.ClassLoaderUtil;

/**
 *
 * @author 高海军 帝奇 74394
 * @date 2017 May  21:00
 */
public class DefaultInstanceAccessor implements InstanceAccessor {

    @Override
    public Object access(String classNameOrBeanName) {
        Object instance = ClassLoaderUtil.createOrGetInstanceWithASM(classNameOrBeanName);
        return instance;
    }
}
