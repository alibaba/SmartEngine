package com.alibaba.smart.framework.process.session.util;

import com.alibaba.smart.framework.process.session.ExecutionSession;

/**
 * @author 高海军 帝奇 Apr 19, 2015 11:43:33 AM
 */
public abstract class ThreadLocalExecutionSessionUtil {
    
    private static final ThreadLocal<ExecutionSession> holder = new ThreadLocal<ExecutionSession>();

    public static void set(ExecutionSession value) {
        holder.set(value);
    }

    public static void remove(ExecutionSession value) {
        holder.remove();
    }

    public static ExecutionSession get() {
        return holder.get();
    }

}
