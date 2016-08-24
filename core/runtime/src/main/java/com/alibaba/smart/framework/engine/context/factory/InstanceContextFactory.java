package com.alibaba.smart.framework.engine.context.factory;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

/**
 * 实例上下文工厂 Created by ettear on 16-4-20.
 * TODO factory 太多
 */
public interface InstanceContextFactory {

    ExecutionContext create();
}
