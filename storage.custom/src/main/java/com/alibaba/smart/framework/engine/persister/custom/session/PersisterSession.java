/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.alibaba.smart.framework.engine.persister.custom.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * @author xuantian 2017-02-08 5:50 xuantian
 */
public class PersisterSession {


    /**
     * The Session Thread Local.
     */
    private static InheritableThreadLocal<Stack<PersisterSession>> sessionThreadLocal = new InheritableThreadLocal<Stack<PersisterSession>>();

    private Map<String, ProcessInstance> processInstances = new HashMap<String, ProcessInstance>(4);

    public static PersisterSession create() {
        PersisterSession session = new PersisterSession();
        session.store();
        return session;
    }

    protected static <T> Stack<T> getStack(ThreadLocal<Stack<T>> threadLocal) {
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

    /**
     * store to the thread local.
     */
    public void store() {
        getStack(sessionThreadLocal).push(this);
    }

    /**
     * destroy from the thread local.
     */
    public void destroy() {
        getStack(sessionThreadLocal).pop();
    }

    /**
     * @return the BizSession got from the thread local.
     */
    public static PersisterSession currentSession() {
        Stack<PersisterSession> sessions = getStack(sessionThreadLocal);
        if (sessions.isEmpty()) {
            return null;
        }
        return sessions.peek();
    }

    /**
     * the static method for destroy session for easy using.
     */
    public static void destroySession() {
        getStack(sessionThreadLocal).pop();
    }

    public Map<String, ProcessInstance> getProcessInstances() {
        return processInstances;
    }


    public void setProcessInstance(ProcessInstance processInstance) {
            putProcessInstance( processInstance) ;
    }

    public void putProcessInstance(ProcessInstance processInstance) {
        this.processInstances.put(processInstance.getInstanceId(), processInstance);
    }

    public ProcessInstance getProcessInstance(String instanceId) {
        return this.processInstances.get(instanceId);
    }
}