package com.alibaba.smart.framework.engine.util;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;

/**
 * @author 高海军 帝奇 Apr 19, 2015 11:43:33 AM
 */
public abstract class ThreadLocalUtil {

    private static final ThreadLocal<ProcessEngineConfiguration> holder = new ThreadLocal<ProcessEngineConfiguration>();

    public static void set(ProcessEngineConfiguration value) {
        holder.set(value);
    }

    public static void remove() {
        holder.remove();
    }

    public static ProcessEngineConfiguration get() {
        return holder.get();
    }

}
