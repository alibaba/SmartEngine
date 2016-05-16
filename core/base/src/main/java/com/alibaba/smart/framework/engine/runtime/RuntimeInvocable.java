package com.alibaba.smart.framework.engine.runtime;

import com.alibaba.smart.framework.engine.assembly.Invocable;
import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.invocation.Message;

/**
 * 运行时Invocable
 * Created by ettear on 16-4-14.
 */
public interface RuntimeInvocable<M extends Invocable> extends LifeCycleListener {

    String getId();

    //FIXME 获取assembly 类型?
    Class<?> getModelType();

    M getModel();

    /**
     * Invoke an operation with a context message
     *
     * @param context The request message
     * @return The response message
     */
    Message invoke(String event, InstanceContext context);

    /**
     * Asynchronously invoke an operation with a context message
     *
     * @param context The request message
     */
    Message invokeAsync(String event, InstanceContext context);

}
