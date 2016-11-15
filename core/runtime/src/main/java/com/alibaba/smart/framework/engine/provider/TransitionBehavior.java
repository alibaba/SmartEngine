package com.alibaba.smart.framework.engine.provider;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.Transition;
import com.alibaba.smart.framework.engine.pvm.PvmTransition;

/**
 * @author 高海军 帝奇  2016.11.11   TODO 看下存在性
 * @author ettear 2016.04.13
 */
public interface TransitionBehavior<M extends Transition>  {

     boolean execute(PvmTransition pvmTransition, ExecutionContext context);
}
