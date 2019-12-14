package com.alibaba.smart.framework.engine.retry.extensionpoint.impl;

import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.impl.AbstractPropertiesExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.retry.RetryExtensionPoint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhenhong.tzh
 * @date 2019-04-27
 */
public class DefaultRetryExtensionPoint extends AbstractPropertiesExtensionPointRegistry
    implements RetryExtensionPoint {

    private Map<Class<?>, Object> retryFactories = MapUtil.newHashMap();

    public DefaultRetryExtensionPoint(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    protected void initExtension(ClassLoader classLoader, String key, Object object) {
        Class<?> extensionValueClass;
        try {
            extensionValueClass = classLoader.loadClass(key);
        } catch (ClassNotFoundException e) {
            throw new EngineException("Scan config file " + getExtensionName() + " failure!", e);
        }
        this.retryFactories.put(extensionValueClass, object);
    }

    @Override
    public <T> T getExtensionPoint(Class<T> modelType) {
        return (T)this.retryFactories.get(modelType);
    }

    @Override
    protected String getExtensionName() {
        return "retry-extensions";
    }
}
