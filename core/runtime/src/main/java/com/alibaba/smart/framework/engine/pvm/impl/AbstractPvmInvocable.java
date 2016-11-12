package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.model.assembly.IndentityElement;
import com.alibaba.smart.framework.engine.provider.InvokerProvider;
import com.alibaba.smart.framework.engine.provider.ProviderRegister;
import com.alibaba.smart.framework.engine.pvm.PvmInvocable;

/**
 * @author 高海军 帝奇  2016.11.11   TODO 看下存在性
 * @author ettear 2016.04.13
 */
public abstract class AbstractPvmInvocable<M extends IndentityElement> implements PvmInvocable<M>, ProviderRegister {

    private static final Logger  LOGGER   = LoggerFactory.getLogger(AbstractPvmInvocable.class);

    private M                    model;

    private InvokerProvider      provider;

    private Map<String, Invoker> invokers = new ConcurrentHashMap<>();

    @Override
    public void start() {
        this.provider.start();
    }

    @Override
    public void stop() {
        this.provider.stop();
    }

    @Override
    public void fireEvent(String event, ExecutionContext context) {
        Invoker invoker = this.getInvoker(event);
        if (null == invoker) {
            LOGGER.debug("invoker is null for event:" + event + ",so now return.");
            return ;
        } else {
            LOGGER.debug("invoker is ok for event:" + event + ",so deep into.");

             invoker.invoke(context);

        }
    }

    private Invoker getInvoker(String event) {

        //FIXME
        Invoker invoker = this.invokers.get(event);
        if (null != invoker) {
            return invoker;
        }
        invoker = this.provider.createInvoker(event);
        if (null != invoker) {
            this.invokers.put(event, invoker);
        } else {
            invoker = this.createDefaultInvoker(event);
        }
        return invoker;
    }

    protected Invoker createDefaultInvoker(String event) {
        return null;
    }


    @Override
    public M getModel() {
        return this.model;
    }

    public void setModel(M model) {
        this.model = model;
    }

    @Override
    public InvokerProvider getProvider() {
        return provider;
    }

    @Override
    public void registerProvider(InvokerProvider invocableProvider) {
        this.provider = invocableProvider;
    }
    
    @Override
    public String toString() {
        return " [getId()=" + getModel().getId() + ", getModelType()=" + getModel().getClass() + "]";
    }


}
