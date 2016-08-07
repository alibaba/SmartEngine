package com.alibaba.smart.framework.engine.provider.factory;

import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.provider.TransitionProvider;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * Created by ettear on 16-4-11.
 */
public interface TransitionProviderFactory<M extends Transition> extends ProviderFactory<M> {

    TransitionProvider<M> createTransitionProvider(PvmTransition runtimeTransition);

}
