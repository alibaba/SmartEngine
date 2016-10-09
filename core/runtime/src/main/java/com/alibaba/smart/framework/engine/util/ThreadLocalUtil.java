package com.alibaba.smart.framework.engine.util;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;

/**
 * @author 高海军 帝奇 Apr 19, 2015 11:43:33 AM
 */
public class ThreadLocalUtil {

    private static ProcessEngineConfiguration holder ;

    public static void set(ProcessEngineConfiguration value) {
        if (holder == null) {
            holder = value;
        }
    }

    public static void remove() {
        holder = null;
    }

    public static ProcessEngineConfiguration get() {
        return holder;
    }

}
