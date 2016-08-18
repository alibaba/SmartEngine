package com.alibaba.smart.framework.engine.provider;


/**
 * Created by ettear on 16-4-21.
 */
public interface ProviderRegister {

    void registerProvider(InvokerProvider invocableProvider);
    
    InvokerProvider getProvider();
}
