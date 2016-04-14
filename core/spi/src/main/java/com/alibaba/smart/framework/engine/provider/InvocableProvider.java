package com.alibaba.smart.framework.engine.provider;

import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.invocation.Invoker;

/**
 * Created by ettear on 16-4-11.
 */
public interface InvocableProvider extends LifeCycleListener {

    Invoker createInvoker(String event);
}
