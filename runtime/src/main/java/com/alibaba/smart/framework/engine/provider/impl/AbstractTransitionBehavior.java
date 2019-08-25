package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractTransitionBehavior<T extends Transition> implements TransitionBehavior {

    private PvmTransition runtimeTransition;

    private ExtensionPointRegistry extensionPointRegistry;

    public AbstractTransitionBehavior(ExtensionPointRegistry extensionPointRegistry,PvmTransition runtimeTransition) {
        this.extensionPointRegistry=extensionPointRegistry;
        this.runtimeTransition = runtimeTransition;
    }

    public AbstractTransitionBehavior() {
    }

    @Override
    public boolean match(ExecutionContext context) {
        return false;
    }

    @Override
    public Object execute(ExecutionContext context) {
        return null;
    }

    protected PvmTransition getRuntimeTransition() {
        return runtimeTransition;
    }

    protected T getModel() {
        return (T)this.runtimeTransition.getModel();
    }
}
