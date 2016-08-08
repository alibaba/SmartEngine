package com.alibaba.smart.framework.engine.extensionpoint.impl;

import com.alibaba.smart.framework.engine.assembly.Handler;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.invocation.Invoker;

/**
 * HandlerProviderExtensionPoint Created by ettear on 16-4-29.
 */
public interface HandlerProviderExtensionPoint extends LifeCycleListener {

    Invoker createInvoker(Handler handler);
}

 