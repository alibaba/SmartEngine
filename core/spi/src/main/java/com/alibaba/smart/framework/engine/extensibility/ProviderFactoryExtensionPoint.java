package com.alibaba.smart.framework.engine.extensibility;

import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.provider.factory.ProviderFactory;

/**
 * Created by ettear on 16-4-12.
 */
public interface ProviderFactoryExtensionPoint extends ClassLoaderExtensionPoint,LifeCycleListener {
    ProviderFactory getProviderFactory(Class<?> modelType);
}
