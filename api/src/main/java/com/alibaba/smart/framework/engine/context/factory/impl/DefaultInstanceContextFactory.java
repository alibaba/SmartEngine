package com.alibaba.smart.framework.engine.context.factory.impl;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.context.factory.InstanceContextFactory;
import com.alibaba.smart.framework.engine.context.impl.DefaultInstanceContext;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;

/**
 * Created by ettear on 16-4-20.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = InstanceContextFactory.class)
public class DefaultInstanceContextFactory implements InstanceContextFactory {

    @Override
    public ExecutionContext create() {
        return new DefaultInstanceContext();
    }
}
