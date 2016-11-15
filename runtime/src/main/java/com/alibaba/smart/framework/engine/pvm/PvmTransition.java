package com.alibaba.smart.framework.engine.pvm;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface PvmTransition extends PvmInvocable<Transition> {

    PvmActivity getSource();

    PvmActivity getTarget();

    void execute(ExecutionContext context);

    TransitionBehavior getTransitionBehavior();

    void setTransitionBehavior(TransitionBehavior transitionBehavior);

}
