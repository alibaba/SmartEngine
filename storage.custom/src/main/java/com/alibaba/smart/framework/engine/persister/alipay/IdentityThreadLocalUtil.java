package com.alibaba.smart.framework.engine.persister.alipay;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

import java.io.Serializable;
/**
 * Created by 高海军 帝奇 74394 on 2017 February  11:54.
 */
public class IdentityThreadLocalUtil {

    private static  ThreadLocal<ProcessInstance> processInstanceThreadLocal = new ThreadLocal<ProcessInstance>();

    public static void set(ProcessInstance processInstance) {
        processInstanceThreadLocal.set(processInstance);
    }

    public static void remove() {
        processInstanceThreadLocal.remove();
    }

    public static ProcessInstance get() {
        return processInstanceThreadLocal.get();
    }

}
