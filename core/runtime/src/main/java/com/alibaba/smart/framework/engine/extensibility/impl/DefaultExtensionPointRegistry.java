package com.alibaba.smart.framework.engine.extensibility.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.extensibility.ClassLoaderExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;

/**
 * 默认扩展注册器实现 Created by ettear on 16-4-12.
 */
public class DefaultExtensionPointRegistry extends AbstractPropertiesExtensionPoint implements ExtensionPointRegistry {

    private Map<Class<?>, Object> extensionPoints = new ConcurrentHashMap<>();

    public DefaultExtensionPointRegistry(SmartEngine engine) {
        this.setExtensionPointRegistry(this);
        this.extensionPoints.put(SmartEngine.class, engine);
    }

    @Override
    public void load(String moduleName, ClassLoader classLoader) throws ExtensionPointLoadException {
        super.load(moduleName, classLoader);
        for (Object extensionPoint : extensionPoints.values()) {
            if (extensionPoint instanceof ClassLoaderExtensionPoint) {
                ClassLoaderExtensionPoint classLoaderExtensionPoint = (ClassLoaderExtensionPoint) extensionPoint;
                classLoaderExtensionPoint.load(moduleName, classLoader);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getExtensionPoint(Class<T> extensionPointType) {
        return (T) this.extensionPoints.get(extensionPointType);
    }

    @Override
    public void start() {
        for (Object extensionPoint : extensionPoints.values()) {
            if (extensionPoint instanceof LifeCycleListener) {
                LifeCycleListener lifeCycleListener = (LifeCycleListener) extensionPoint;
                lifeCycleListener.start();
            }
        }
    }

    @Override
    public void stop() {
        for (Object extensionPoint : extensionPoints.values()) {
            if (extensionPoint instanceof LifeCycleListener) {
                LifeCycleListener lifeCycleListener = (LifeCycleListener) extensionPoint;
                lifeCycleListener.stop();
            }
        }
    }

    @Override
    protected void initExtension(ClassLoader classLoader, String type, Object object)
                                                                                     throws ExtensionPointLoadException {
        Class interfaze;
        try {
            interfaze = classLoader.loadClass(type);
        } catch (ClassNotFoundException e) {
            throw new ExtensionPointLoadException("Class[" + type + "] not found!", e);
        }
        this.extensionPoints.put(interfaze, object);
    }

    @Override
    protected String getExtensionName() {
        return "extensions";
    }
}
