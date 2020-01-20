package com.alibaba.smart.framework.engine.behavior;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface TransitionBehavior {

    boolean match(ExecutionContext context, ConditionExpression conditionExpression);

}
