package com.alibaba.smart.framework.process.session.util;

/**
 * @author 高海军 帝奇 Apr 19, 2015 11:43:33 AM
 */
public abstract class ThreadLocalExecutionSessionUtil {
    //TODO 添加泛型约束?
    private static final ThreadLocal<Object> holder = new ThreadLocal<Object>();

    public static void set(Object value) {
        holder.set(value);
    }

    public static void remove(Object value) {
        holder.remove();
    }

    public static Object get() {
        return holder.get();
    }

}
