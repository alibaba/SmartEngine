/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.alibaba.smart.framework.engine.persister.custom.session;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * @author xuantian 2017-02-08 5:50 xuantian
 */
public class PersisterSession {


    /**
     * The Session Thread Local.
     */
    private static ThreadLocal<PersisterSession> sessionThreadLocal = new ThreadLocal<PersisterSession>();

    private Map<Long, ProcessInstance> processInstances = new HashMap<Long, ProcessInstance>();

    public static PersisterSession create() {
        PersisterSession session = new PersisterSession();
        session.store();
        return session;
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
    public static PersisterSession currentSession() {
        return  sessionThreadLocal.get();
    }

    /**
     * the static method for destroy session for easy using.
     */
    public static void destroySession() {
        PersisterSession session = sessionThreadLocal.get();
        if (null != session) {
            session.destroy();
        }

        sessionThreadLocal.set(null);
    }

    public Map<Long, ProcessInstance> getProcessInstances() {
        return processInstances;
    }

    public void putProcessInstance(ProcessInstance processInstance) {
        this.processInstances.put(processInstance.getInstanceId(), processInstance);
    }

    public ProcessInstance getProcessInstance(Long instanceId) {
        return this.processInstances.get(instanceId);
    }
}