package com.alibaba.smart.framework.engine.context.factory.impl;

import com.alibaba.smart.framework.engine.context.Fact;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.context.impl.DefaultInstanceContext;

/**
 * Created by ettear on 16-4-20.
 */
public class DefaultInstanceContextFactory implements InstanceContextFactory {
    @Override
    public InstanceContext create() {
        return new DefaultInstanceContext();
    }
}
