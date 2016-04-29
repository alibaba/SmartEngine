package com.alibaba.smart.framework.engine.provider.factory;

/**
 * Provider工厂
 * Created by ettear on 16-4-11.
 */
public interface ProviderFactory<M> {
    Class<M> getModelType();
}
