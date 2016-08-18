package com.alibaba.smart.framework.engine.pvm;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.assembly.IndentityElement;

/**
 * 运行时Invocable Created by ettear on 16-4-14.
 */
public interface PvmInvocable<M extends IndentityElement> extends LifeCycleListener {


    M getModel();

    /**
     * Invoke an operation with a context message
     *
     * @param context The request message
     * @return The response message
     */
    Message fireEvent(String event, ExecutionContext context);

    /**
     * Asynchronously invoke an operation with a context message
     *
     * @param context The request message
     */
    Message invokeAsync(String event, ExecutionContext context);

}
