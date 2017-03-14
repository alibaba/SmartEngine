package com.alibaba.smart.framework.engine.instance.factory.impl;

import com.alibaba.smart.framework.engine.common.id.generator.IdGenerator;
import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.instance.factory.TransitionInstanceFactory;
import com.alibaba.smart.framework.engine.instance.impl.DefaultTransitionInstance;
import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;

/**
 * 默认关联实例工厂实现 Created by ettear on 16-4-20.
 */
public class DefaultTransitionInstanceFactory implements TransitionInstanceFactory {

    @Override
    public TransitionInstance create(ExecutionContext executionContext) {
        DefaultTransitionInstance defaultTransitionInstance = new DefaultTransitionInstance();

        IdGenerator idGenerator = executionContext.getProcessEngineConfiguration().getIdGenerator();

        defaultTransitionInstance.setInstanceId(idGenerator.getId());
        return defaultTransitionInstance;
    }
}
