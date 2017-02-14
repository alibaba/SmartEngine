/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.alibaba.smart.framework.engine.persister.util;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * @author xuantian 2017-02-08 5:50 xuantian
 */
public class WorkFlowSession {


    /**
     * The Session Thread Local.
     */
    private static ThreadLocal<WorkFlowSession> sessionThreadLocal = new ThreadLocal<WorkFlowSession>();


    private ProcessInstance processInstance;

    public static WorkFlowSession create() {
        WorkFlowSession session = new WorkFlowSession();
        session.store();
        return session;
    }

    /**
     * default constructor.
     */
    private WorkFlowSession() {

    }

    /**
     * store to the thread local.
     */
    public void store() {
        if (null != sessionThreadLocal.get()) {
            throw new RuntimeException("Session existed in the ThreadLocal. A new session can't be created.");
        }
        sessionThreadLocal.set(this);
    }

    /**
     * destroy from the thread local.
     */
    public void destroy() {
        sessionThreadLocal.set(null);
    }

    /**
     * @return the BizSession got from the thread local.
     */
    public static WorkFlowSession currentSession() {
        return  sessionThreadLocal.get();
    }

    /**
     * the static method for destroy session for easy using.
     */
    public static void destroySession() {
        WorkFlowSession session = sessionThreadLocal.get();
        if (null != session) {
            session.destroy();
        }

        sessionThreadLocal.set(null);
    }

    /**
     * Getter method for property processInstance.
     *
     * @return property value of processInstance
     */
    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    /**
     * Setter method for property processInstance.
     *
     * @param processInstance value to be assigned to property processInstance
     */
    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }
}