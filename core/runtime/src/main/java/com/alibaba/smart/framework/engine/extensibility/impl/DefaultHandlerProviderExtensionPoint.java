package com.alibaba.smart.framework.engine.extensibility.impl;

import com.alibaba.smart.framework.engine.assembly.Handler;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.HandlerProviderExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;
import com.alibaba.smart.framework.engine.invocation.Invoker;
import com.alibaba.smart.framework.engine.provider.HandlerProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DefaultHandlerProviderExtensionPoint
 * Created by ettear on 16-4-29.
 */
public class DefaultHandlerProviderExtensionPoint extends AbstractPropertiesExtensionPoint
        implements HandlerProviderExtensionPoint {

    private Map<Class, HandlerProvider> providers = new ConcurrentHashMap<>();

    public DefaultHandlerProviderExtensionPoint(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public Invoker createInvoker(Handler handler) {
        if (null != this.providers.get(handler.getClass())) {
            return this.providers.get(handler.getClass()).createInvoker(handler);
        } else {
            return null;
        }
    }

    @Override
    protected void initExtension(ClassLoader classLoader, String type, Object handlerProviderObject)
            throws ExtensionPointLoadException {
        if (handlerProviderObject instanceof HandlerProvider) {
            HandlerProvider handlerProvider = (HandlerProvider) handlerProviderObject;
            this.providers.put(handlerProvider.getType(), handlerProvider);
        }
    }

    @Override
    public void start() {
        for (HandlerProvider provider : providers.values()) {
            if (provider instanceof LifeCycleListener) {
                ((LifeCycleListener) provider).start();
            }
        }
    }

    @Override
    public void stop() {
        for (HandlerProvider provider : providers.values()) {
            if (provider instanceof LifeCycleListener) {
                ((LifeCycleListener) provider).stop();
            }
        }
    }

    @Override
    protected String getExtensionName() {
        return "handler-provider";
    }
}
