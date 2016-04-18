package com.alibaba.smart.framework.engine.invocation;

import com.alibaba.smart.framework.engine.context.InstanceContext;

/**
 * Invoker
 * Created by ettear on 16-4-11.
 */
public interface Invoker {

    Message invoke(InstanceContext context);
}
