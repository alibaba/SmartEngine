package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.assembly.Transition;
import com.alibaba.smart.framework.engine.invocation.AtomicOperationEvent;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.provider.TransitionProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;

/**
 * 抽象Transition Provider实现 Created by ettear on 16-4-20.
 */
public abstract class AbstractTransitionProvider<T extends Transition> implements TransitionProvider<T> {

    private RuntimeTransition runtimeTransition;

    public AbstractTransitionProvider(RuntimeTransition runtimeTransition) {
        this.runtimeTransition = runtimeTransition;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Invoker createInvoker(String event) {
        if (AtomicOperationEvent.TRANSITION_HIT.name().equals(event)) {
            return this.createHitInvoker();
        } else if (AtomicOperationEvent.TRANSITION_START.name().equals(event)) {
            return this.createStartInvoker();
        } else if (AtomicOperationEvent.TRANSITION_EXECUTE.name().equals(event)) {
            return this.createExecuteInvoker();
        } else if (AtomicOperationEvent.TRANSITION_END.name().equals(event)) {
            return this.createEndInvoker();
        } else {
            return this.createEventInvoker(event);
        }
    }

    protected Invoker createHitInvoker() {
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

    protected RuntimeTransition getRuntimeTransition() {
        return runtimeTransition;
    }
}
