package com.alibaba.smart.framework.engine.provider.factory;

import com.alibaba.smart.framework.engine.assembly.Transition;
import com.alibaba.smart.framework.engine.provider.TransitionProvider;
import com.alibaba.smart.framework.engine.runtime.RuntimeTransition;

/**
 * Created by ettear on 16-4-11.
 */
public interface TransitionProviderFactory<M extends Transition> extends ProviderFactory<M> {

    TransitionProvider<M> createTransitionProvider(RuntimeTransition runtimeTransition);

}
