package com.alibaba.smart.framework.engine.provider;

import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;

/**
 * Invoker Provider Created by ettear on 16-4-11.
 */
public interface InvokerProvider extends LifeCycleListener {

    /**
     * 创建对应事件的Invoker
     * 
     * @param event 事件
     * @return Invoker
     */
    Invoker createInvoker(String event);


    default boolean containAction(String action){
        return true;
    }
}
