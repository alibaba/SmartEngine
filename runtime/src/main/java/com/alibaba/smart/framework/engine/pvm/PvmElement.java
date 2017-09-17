package com.alibaba.smart.framework.engine.pvm;

import java.util.List;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.assembly.Element;
import com.alibaba.smart.framework.engine.provider.Invoker;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface PvmElement<M extends Element> extends LifeCycleListener {

    M getModel();

    /**
     * Invoke an operation with a context message
     *
     * @param context The request message
     * @return The response message
     */
    Object invoke(String event, ExecutionContext context);

    void setInvoker(Invoker invoker);

    void setPrepareExtensionInvokers(List<Invoker> prepareExtensionInvokers);

    void setExtensionInvokers(List<Invoker> extensionInvokers);
}
