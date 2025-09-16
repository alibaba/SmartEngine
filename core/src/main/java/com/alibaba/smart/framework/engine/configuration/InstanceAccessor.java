package com.alibaba.smart.framework.engine.configuration;

/**
 * @author 高海军 帝奇 74394
 * @date 2017 May 20:52
 */
public interface InstanceAccessor {

    Object access(String classNameOrBeanName);
}
