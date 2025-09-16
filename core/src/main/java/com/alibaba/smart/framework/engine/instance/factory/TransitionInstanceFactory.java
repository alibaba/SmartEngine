package com.alibaba.smart.framework.engine.instance.factory;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.model.instance.TransitionInstance;

/** 关联实例工 Created by ettear on 16-4-20. */
public interface TransitionInstanceFactory {

    /**
     * 创建关联实例
     *
     * @return 关联实例
     */
    TransitionInstance create(ExecutionContext executionContext);
}
