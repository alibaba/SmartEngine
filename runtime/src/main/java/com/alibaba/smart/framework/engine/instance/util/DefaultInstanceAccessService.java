package com.alibaba.smart.framework.engine.instance.util;

import com.alibaba.smart.framework.engine.common.service.InstanceAccessService;

/**
 *
 * @author 高海军 帝奇 74394
 * @date 2017 May  21:00
 */
public class DefaultInstanceAccessService implements InstanceAccessService {

    @Override
    public Object access(String classNameOrBeanName) {
        Object instance = ClassLoaderUtil.createOrGetInstanceWithASM(classNameOrBeanName);
        return instance;
    }
}
