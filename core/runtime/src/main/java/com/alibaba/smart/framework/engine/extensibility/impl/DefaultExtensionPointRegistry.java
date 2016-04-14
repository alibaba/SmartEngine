package com.alibaba.smart.framework.engine.extensibility.impl;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.deployment.impl.DefaultDeployer;
import com.alibaba.smart.framework.engine.extensibility.ClassLoaderExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.extensibility.AssemblyProcessorExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.ProviderFactoryExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.exception.ExtensionPointLoadException;
import com.alibaba.smart.framework.engine.instance.InstanceManager;
import com.alibaba.smart.framework.engine.instance.impl.DefaultInstanceManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认扩展注册器实现
 * Created by ettear on 16-4-12.
 */
public class DefaultExtensionPointRegistry implements ExtensionPointRegistry{

    private Map<Class<?>, Object> extensionPoints = new ConcurrentHashMap<>();

    public DefaultExtensionPointRegistry(SmartEngine engine){
        this.extensionPoints.put(SmartEngine.class, engine);
        this.extensionPoints.put(AssemblyProcessorExtensionPoint.class, new DefaultAssemblyProcessorExtensionPoint(this));
        this.extensionPoints.put(ProviderFactoryExtensionPoint.class, new DefaultProviderFactoryExtensionPoint(this));
        this.extensionPoints.put(Deployer.class,new DefaultDeployer(this));
        this.extensionPoints.put(InstanceManager.class,new DefaultInstanceManager(this));
    }

    @Override
    public void load(String moduleName,ClassLoader classLoader) throws ExtensionPointLoadException {
        for (Object extensionPoint : extensionPoints.values()) {
            if(extensionPoint instanceof ClassLoaderExtensionPoint){
                ClassLoaderExtensionPoint classLoaderExtensionPoint=(ClassLoaderExtensionPoint)extensionPoint;
                classLoaderExtensionPoint.load(moduleName,classLoader);
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
            if(extensionPoint instanceof LifeCycleListener){
                LifeCycleListener lifeCycleListener=(LifeCycleListener)extensionPoint;
                lifeCycleListener.start();
            }
        }
    }

    @Override
    public void stop() {
        for (Object extensionPoint : extensionPoints.values()) {
            if(extensionPoint instanceof LifeCycleListener){
                LifeCycleListener lifeCycleListener=(LifeCycleListener)extensionPoint;
                lifeCycleListener.stop();
            }
        }
    }
}
