package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.assembly.Activity;
import com.alibaba.smart.framework.engine.invocation.AtomicOperationEvent;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.provider.ActivityProvider;
import com.alibaba.smart.framework.engine.pvm.PvmActivity;

/**
 * 抽象Activity Provider实现 Created by ettear on 16-4-20. TODO 职责略不清晰
 */
public class AbstractActivityProvider<T extends Activity> implements ActivityProvider<T> {

    private PvmActivity runtimeActivity;

    public AbstractActivityProvider(PvmActivity runtimeActivity) {
        this.runtimeActivity = runtimeActivity;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Invoker createInvoker(String event) {
        if (AtomicOperationEvent.ACTIVITY_START.name().equals(event)) {
            return this.createStartInvoker();
        } else if (AtomicOperationEvent.ACTIVITY_EXECUTE.name().equals(event)) {
            return this.createExecuteInvoker();
        } else if (AtomicOperationEvent.ACTIVITY_END.name().equals(event)) {
            return this.createEndInvoker();
        } else if (AtomicOperationEvent.ACTIVITY_TRANSITION_SELECT.name().equals(event)) {
            return this.createTransitionSelectInvoker();
        } else {
            return this.createEventInvoker(event);
        }
    }

    protected Invoker createTransitionSelectInvoker() {
        return null;
    }

    protected Invoker createStartInvoker() {
        return null;
    }

    protected Invoker createExecuteInvoker() {
        return null;
    }

    protected Invoker createEndInvoker() {
        return null;
    }

    protected Invoker createEventInvoker(String event) {
        return null;
    }

    protected PvmActivity getRuntimeActivity() {
        return runtimeActivity;
    }
}
