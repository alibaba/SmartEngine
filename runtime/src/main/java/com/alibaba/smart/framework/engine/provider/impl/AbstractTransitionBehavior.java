package com.alibaba.smart.framework.engine.provider.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.behavior.TransitionBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractTransitionBehavior<T extends Transition> implements TransitionBehavior {

    private PvmTransition runtimeTransition;



    @Override
    public boolean match(ExecutionContext context, ConditionExpression conditionExpression) {
        return false;
    }



    protected T getModel() {
        return (T)this.runtimeTransition.getModel();
    }
}
