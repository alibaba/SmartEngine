package com.alibaba.smart.framework.engine.context.factory;

import com.alibaba.smart.framework.engine.context.Context;
import com.alibaba.smart.framework.engine.context.InstanceContext;

/**
 * 实例上下文工厂
 * Created by ettear on 16-4-20.
 */
public interface InstanceContextFactory {
    InstanceContext create();
    InstanceContext create(Context context);
}
