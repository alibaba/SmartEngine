package com.alibaba.smart.framework.engine.extensionpoint.impl;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.assembly.Handler;

/**
 * HandlerProviderExtensionPoint Created by ettear on 16-4-29.
 */
public interface HandlerProviderExtensionPoint extends LifeCycleListener {

    Invoker createInvoker(Handler handler);
}
