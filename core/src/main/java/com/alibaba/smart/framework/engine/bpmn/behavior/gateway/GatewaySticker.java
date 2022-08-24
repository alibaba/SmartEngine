/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.alibaba.smart.framework.engine.bpmn.behavior.gateway;

import java.util.Stack;

import com.alibaba.smart.framework.engine.pvm.PvmActivity;

import lombok.Getter;
import lombok.Setter;

public class GatewaySticker {


    private static InheritableThreadLocal<Stack<GatewaySticker>> sessionThreadLocal = new InheritableThreadLocal<Stack<GatewaySticker>>();

//    @Getter
//    @Setter
//    private  volatile PvmActivity pvmActivity;


    public static GatewaySticker create() {
        GatewaySticker session = new GatewaySticker();
        session.store();
        return session;
    }

    private static <T> Stack<T> initOrGetStack(ThreadLocal<Stack<T>> threadLocal) {
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
    private GatewaySticker() {

    }

    /**
     * store to the thread local.
     */
    private void store() {
        initOrGetStack(sessionThreadLocal).push(this);
    }


    /**
     * @return the BizSession got from the thread local.
     */
    public static GatewaySticker currentSession() {
        Stack<GatewaySticker> sessions = initOrGetStack(sessionThreadLocal);
        if (sessions.isEmpty()) {
            return null;
        }
        return sessions.peek();
    }

    /**
     * the static method for destroy session for easy using.
     */
    public static void destroySession() {
        Stack<GatewaySticker> gatewayStickers = initOrGetStack(sessionThreadLocal);
        gatewayStickers.pop();

    }

}