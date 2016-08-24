package com.alibaba.smart.framework.engine.provider;

import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.provider.factory.ProviderFactory;

/**
 * Created by ettear on 16-4-12.
 * 
 * TODO ProviderFactory-->ProviderFactory-->Invoker 略繁琐
 */
public interface ProviderFactoryExtensionPoint extends LifeCycleListener {

    ProviderFactory<?> getProviderFactory(Class<?> modelType);
}
