/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.alibaba.smart.framework.engine.persister.alipay;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * 工作流上下文
 * @author xuantian
 * @version $Id: WorkFlowSession.java, v 0.1 2017-02-08 下午5:50 xuantian Exp $$
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
    public WorkFlowSession() {

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
        WorkFlowSession bizSession = sessionThreadLocal.get();
        if (null != bizSession) {
            bizSession.destroy();
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