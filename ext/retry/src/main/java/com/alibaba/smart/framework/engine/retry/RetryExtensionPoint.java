package com.alibaba.smart.framework.engine.retry;

import com.alibaba.smart.framework.engine.hook.LifeCycleHook;

/**
 * @author zhenhong.tzh
 * @date 2019-04-27
 */
public interface RetryExtensionPoint extends LifeCycleHook {

    /**
     * 获取重试扩展点
     *
     * @param modelType 扩展点类型
     * @return 扩展点
     */
    <T> T getExtensionPoint(Class<T> modelType);
}
