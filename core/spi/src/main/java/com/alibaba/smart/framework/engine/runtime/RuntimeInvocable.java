package com.alibaba.smart.framework.engine.runtime;

import com.alibaba.smart.framework.engine.assembly.Invocable;
import com.alibaba.smart.framework.engine.context.Context;
import com.alibaba.smart.framework.engine.invocation.Message;

/**
 * 运行时Invocable
 * Created by ettear on 16-4-14.
 */
public interface RuntimeInvocable<M extends Invocable> {
    String getId();

    Class<?> getModelType();

    M getModel();

    /**
     * Invoke an operation with a context message
     * @param context The request message
     * @return The response message
     */
    Message invoke(String event,Context context);

    /**
     * Asynchronously invoke an operation with a context message
     * @param context The request message
     */
    Message invokeAsync(String event,Context context);

}
