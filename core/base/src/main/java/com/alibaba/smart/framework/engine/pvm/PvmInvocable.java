package com.alibaba.smart.framework.engine.pvm;

import com.alibaba.smart.framework.engine.context.InstanceContext;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.invocation.Message;
import com.alibaba.smart.framework.engine.model.artifact.Indentity;

/**
 * 运行时Invocable Created by ettear on 16-4-14.
 */
public interface PvmInvocable<M extends Indentity> extends LifeCycleListener {

    String getId();

    // 源自java services规范
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
