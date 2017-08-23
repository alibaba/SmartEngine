package com.alibaba.smart.framework.engine.provider.factory;

import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.provider.TransitionBehavior;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface TransitionProviderFactory<M extends Transition> extends ProviderFactory<M> {

    TransitionBehavior createTransitionProvider(PvmTransition runtimeTransition);

}
