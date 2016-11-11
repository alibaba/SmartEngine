package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.provider.TransitionProvider;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;

/**
 * @author 高海军 帝奇  2016.11.11 TODO 职责略不清晰
 * @author ettear 2016.04.13
 */
public abstract class AbstractTransitionProvider<T extends Transition> implements TransitionProvider<T> {

    private PvmTransition runtimeTransition;

    public AbstractTransitionProvider(PvmTransition runtimeTransition) {
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
        if (PvmEventConstant.TRANSITION_HIT.name().equals(event)) {
            return this.createHitInvoker();
        } else if (PvmEventConstant.TRANSITION_START.name().equals(event)) {
            return this.createStartInvoker();
        } else if (PvmEventConstant.TRANSITION_EXECUTE.name().equals(event)) {
            return this.createExecuteInvoker();
        } else if (PvmEventConstant.TRANSITION_END.name().equals(event)) {
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

    protected PvmTransition getRuntimeTransition() {
        return runtimeTransition;
    }
}
