package com.alibaba.smart.framework.engine.context.factory.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.context.impl.DefaultInstanceContext;

/**
 * Created by ettear on 16-4-20.
 */
public class DefaultInstanceContextFactory implements InstanceContextFactory {

    @Override
    public ExecutionContext create() {
        return new DefaultInstanceContext();
    }
}
