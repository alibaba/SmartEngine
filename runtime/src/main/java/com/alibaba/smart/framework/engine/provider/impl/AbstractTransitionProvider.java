package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractTransitionProvider<T extends Transition> implements TransitionBehavior<T> {

    private PvmTransition runtimeTransition;

    public AbstractTransitionProvider(PvmTransition runtimeTransition) {
        this.runtimeTransition = runtimeTransition;
    }


    protected PvmTransition getRuntimeTransition() {
        return runtimeTransition;
    }
}
