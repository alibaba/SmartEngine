package com.alibaba.smart.framework.engine.provider;

import com.alibaba.smart.framework.engine.assembly.Handler;
import com.alibaba.smart.framework.engine.invocation.Invoker;

/**
 * HandlerProvider
 * Created by ettear on 16-4-29.
 */
public interface HandlerProvider<T extends Handler> {
    Class<T> getType();
    Invoker createInvoker(T handler);
}
