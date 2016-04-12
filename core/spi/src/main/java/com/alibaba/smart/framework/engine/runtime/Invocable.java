package com.alibaba.smart.framework.engine.runtime;

import com.alibaba.smart.framework.engine.context.Context;
import com.alibaba.smart.framework.engine.invocation.Message;

/**
 * Created by ettear on 16-4-11.
 */
public interface Invocable {
    /**
     * Invoke an operation with a context message
     * @param context The request message
     * @return The response message
     */
    Message invoke(Context context);

    /**
     * Asynchronously invoke an operation with a context message
     * @param context The request message
     */
    Message invokeAsync(Context context);
}
