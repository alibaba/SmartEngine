package com.alibaba.smart.framework.engine.pvm.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;

import com.alibaba.smart.framework.engine.context.ExecutionContext;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.invocation.impl.DoNothingInvoker;
import com.alibaba.smart.framework.engine.invocation.message.Message;
import com.alibaba.smart.framework.engine.model.assembly.IndentityElement;
import com.alibaba.smart.framework.engine.provider.InvokerProvider;
import com.alibaba.smart.framework.engine.provider.ProviderRegister;
import com.alibaba.smart.framework.engine.pvm.PvmInvocable;

/**
 * DefaultRuntimeInvocable Created by ettear on 16-4-14.
 */
public abstract class AbstractPvmInvocable<M extends IndentityElement> implements PvmInvocable<M> ,ProviderRegister
{

    private M                      model;
    

    private InvokerProvider        provider;
    

    private Map<String, Invoker>   invokers = new ConcurrentHashMap<>();

    @Override
    public String getId() {
        return this.model.getId();
    }

    @Override
    public Class<?> getModelType() {
        return this.model.getClass();
    }
    
    @Override
    public InvokerProvider getProvider() {
        return provider;
    }
    
    @Override
    public void registerProvider(InvokerProvider invocableProvider) {
            this.provider =        invocableProvider;
    }

    @Override
    public M getModel() {
        return this.model;
    }

    public void setModel(M model) {
        this.model = model;
    }

    
    @Override
    public void start() {
        this.provider.start();
    }

    @Override
    public void stop() {
        this.provider.stop();
    }

    @Override
    public Message fireEvent(String event, ExecutionContext context) {
        return this.getInvoker(event).invoke(context);
    }

    @Override
    public Message invokeAsync(String event, ExecutionContext context) {
        return this.getInvoker(event).invoke(context);
    }

    private Invoker getInvoker(String event) {
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
        return DoNothingInvoker.instance;
    }

    @Override
    public String toString() {
        return " [getId()=" + getId() + ", getModelType()=" + getModelType() + "]";
    }

}
