package com.alibaba.smart.framework.engine.provider;

import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.invocation.Invoker;

/**
 * Invoker Provider Created by ettear on 16-4-11.
 */
public interface InvocableProvider extends LifeCycleListener {

    /**
     * 创建对应事件的Invoker
     * 
     * @param event 事件
     * @return Invoker
     */
    Invoker createInvoker(String event);
}
