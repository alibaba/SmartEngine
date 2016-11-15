package com.alibaba.smart.framework.engine.pvm;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.assembly.IndentityElement;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface PvmInvocable<M extends IndentityElement> extends LifeCycleListener {

    //TODO 看下子类
    M getModel();

    /**
     * Invoke an operation with a context message
     *
     * @param context The request message
     * @return The response message
     */
    void fireEvent(String event, ExecutionContext context);

}
