package com.alibaba.smart.framework.engine.provider;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface ActivityBehavior {

    boolean enter(ExecutionContext context);

    boolean execute(ExecutionContext context);

    void leave(ExecutionContext context);

}
