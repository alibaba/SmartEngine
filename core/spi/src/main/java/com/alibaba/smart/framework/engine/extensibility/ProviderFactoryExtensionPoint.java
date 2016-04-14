package com.alibaba.smart.framework.engine.extensibility;

import com.alibaba.smart.framework.engine.provider.ProviderFactory;

/**
 * Created by ettear on 16-4-12.
 */
public interface ProviderFactoryExtensionPoint extends ClassLoaderExtensionPoint {
    ProviderFactory getProviderFactory(Class<?> modelType);
}
