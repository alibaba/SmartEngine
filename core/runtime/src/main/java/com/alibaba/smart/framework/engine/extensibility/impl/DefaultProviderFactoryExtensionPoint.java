package com.alibaba.smart.framework.engine.extensibility.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;
import com.alibaba.smart.framework.engine.provider.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.provider.factory.ProviderFactory;

/**
 * 默认Provider工厂扩展点 Created by ettear on 16-4-12.
 */
@SuppressWarnings("rawtypes")

public class DefaultProviderFactoryExtensionPoint extends AbstractPropertiesExtensionPoint implements ProviderFactoryExtensionPoint {

    /**
     * Artifact处理器
     */
    private Map<Class, ProviderFactory> providerFactories = new ConcurrentHashMap<>();

    public DefaultProviderFactoryExtensionPoint(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public void start() {
        for (ProviderFactory providerFactory : providerFactories.values()) {
            if (providerFactory instanceof LifeCycleListener) {
                ((LifeCycleListener) providerFactory).start();
            }
        }
    }

    @Override
    public void stop() {
        for (ProviderFactory providerFactory : providerFactories.values()) {
            if (providerFactory instanceof LifeCycleListener) {
                ((LifeCycleListener) providerFactory).stop();
            }
        }
    }

    @Override
    protected void initExtension(ClassLoader classLoader, String type, Object providerFactoryObject)
                                                                                                    throws ExtensionPointLoadException {
        if (providerFactoryObject instanceof ProviderFactory) {
            ProviderFactory providerFactory = (ProviderFactory) providerFactoryObject;
            this.providerFactories.put(providerFactory.getModelType(), providerFactory);
        }
    }

    @Override
    protected String getExtensionName() {
        return "provider-factory";
    }

    @Override
    @SuppressWarnings("unchecked")
    public ProviderFactory getProviderFactory(Class<?> modelType) {
        return this.providerFactories.get(modelType);
    }
}
