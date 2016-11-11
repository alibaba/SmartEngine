package com.alibaba.smart.framework.engine.provider;


/**
 * @author 高海军 帝奇  2016.11.11   TODO 看下存在性
 * @author ettear 2016.04.13
 */
public interface ProviderRegister {

    void registerProvider(InvokerProvider invocableProvider);
    
    InvokerProvider getProvider();
}
