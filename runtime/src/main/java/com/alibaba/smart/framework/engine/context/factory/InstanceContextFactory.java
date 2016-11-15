package com.alibaba.smart.framework.engine.context.factory;

import com.alibaba.smart.framework.engine.context.ExecutionContext;

/**
 * 实例上下文工厂 Created by ettear on 16-4-20.
 * TODO JAR 包依赖问题减少
 * TODO 每个xml element 扩展点需要定义3个扩展点,略繁琐
 */
public interface InstanceContextFactory {

    ExecutionContext create();
}
