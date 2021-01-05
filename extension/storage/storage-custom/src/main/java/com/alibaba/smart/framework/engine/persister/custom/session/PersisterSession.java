/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.alibaba.smart.framework.engine.persister.custom.session;

import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * @author xuantian 2017-02-08 5:50 xuantian
 */
public class PersisterSession {


    /**
     * The Session Thread Local.
     */
    private static InheritableThreadLocal<Stack<PersisterSession>> sessionThreadLocal = new InheritableThreadLocal<Stack<PersisterSession>>();

    private Map<String, ProcessInstance> processInstances = new ConcurrentHashMap<String, ProcessInstance>(8);

    public static PersisterSession create() {
        PersisterSession session = new PersisterSession();
        getOrBuildStack(sessionThreadLocal).push(session);
        return session;

    }

    /**
     * @return the BizSession got from the thread local.
     */
    public static PersisterSession currentSession() {
        Stack<PersisterSession> sessions = getOrBuildStack(sessionThreadLocal);
        if (sessions.isEmpty()) {
            return null;
        }
        return sessions.peek();
    }

    /**
     * the static method for destroy session for easy using.
     */
    public static void destroySession() {
        Stack<PersisterSession> stack = getOrBuildStack(sessionThreadLocal);
        stack.pop();

        // 当线程被复用时，导致stack被多个子线程共享，进而导致push/pop 不被成对调用了，最终导致数据错乱。
        if(stack.isEmpty()){
            sessionThreadLocal.remove();
        }
    }


    private static <T> Stack<T> getOrBuildStack(ThreadLocal<Stack<T>> threadLocal) {
        Stack<T> stack = threadLocal.get();
        if (stack==null) {
            stack = new Stack<T>();
            threadLocal.set(stack);
        }
        return stack;
    }

    /**
     * default constructor.
     */
    private PersisterSession() {

    }



    public Map<String, ProcessInstance> getProcessInstances() {
        return processInstances;
    }


    public void putProcessInstance(ProcessInstance processInstance) {
        this.processInstances.put(processInstance.getInstanceId(), processInstance);
    }

    public ProcessInstance getProcessInstance(String instanceId) {
        return this.processInstances.get(instanceId);
    }
}