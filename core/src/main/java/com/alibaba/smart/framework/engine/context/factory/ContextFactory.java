package com.alibaba.smart.framework.engine.context.factory;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;

/**
 * 实例上下文工厂 Created by ettear on 16-4-20.
 */
public interface ContextFactory {

    ExecutionContext create();

    ExecutionContext createFromParentContext(ExecutionContext parentContext);
}
