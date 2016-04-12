package com.alibaba.smart.framework.engine.provider;

/**
 * Created by ettear on 16-4-11.
 */
public interface ProviderFactory<M> {
    Class<M> getModelType();
}
