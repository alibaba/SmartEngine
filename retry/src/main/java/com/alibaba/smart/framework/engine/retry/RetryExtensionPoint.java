package com.alibaba.smart.framework.engine.retry;

import com.alibaba.smart.framework.engine.listener.LifeCycleListener;

/**
 * @author zhenhong.tzh
 * @date 2019-04-27
 */
public interface RetryExtensionPoint extends LifeCycleListener {

    <T> T getExtensionPoint(Class<T> modelType);
}
