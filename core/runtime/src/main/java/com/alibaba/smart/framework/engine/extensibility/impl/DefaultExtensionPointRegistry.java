package com.alibaba.smart.framework.engine.extensibility.impl;

import com.alibaba.smart.framework.engine.extensibility.ClassLoaderExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.ProcessorExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认扩展注册器实现
 * Created by ettear on 16-4-12.
 */
public class DefaultExtensionPointRegistry implements ExtensionPointRegistry {

    private Map<Class<?>, Object> extensionPoints = new ConcurrentHashMap<>();

    public DefaultExtensionPointRegistry(){
        ProcessorExtensionPoint processorExtensionPoint = new DefaultProcessorExtensionPoint(this);
        this.extensionPoints.put(ProcessorExtensionPoint.class, processorExtensionPoint);

        ProviderFactoryExtensionPoint providerFactoryExtensionPoint = new DefaultProviderFactoryExtensionPoint(this);
        this.extensionPoints.put(ProviderFactoryExtensionPoint.class, providerFactoryExtensionPoint);

    }

    @Override
    public void load(ClassLoader classLoader) throws ExtensionPointLoadException {
        for (Object extensionPoint : extensionPoints.values()) {
            if(extensionPoint instanceof ClassLoaderExtensionPoint){
                ClassLoaderExtensionPoint classLoaderExtensionPoint=(ClassLoaderExtensionPoint)extensionPoint;
                classLoaderExtensionPoint.load(classLoader);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getExtensionPoint(Class<T> extensionPointType) {
        return (T) this.extensionPoints.get(extensionPointType);
    }
}
