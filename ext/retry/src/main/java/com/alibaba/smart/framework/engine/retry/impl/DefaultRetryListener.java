package com.alibaba.smart.framework.engine.retry.impl;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.retry.RetryExtensionPoint;
import com.alibaba.smart.framework.engine.retry.RetryListener;
import com.alibaba.smart.framework.engine.retry.model.instance.RetryRecord;
import com.alibaba.smart.framework.engine.retry.service.command.RetryService;

/**
 * 默认重试消息监听实现
 *
 * @author zhenhong.tzh
 * @date 2019-04-27
 */
public class DefaultRetryListener implements RetryListener, LifeCycleListener {

    private final ExtensionPointRegistry extensionPointRegistry;
    private RetryService retryService;

    public DefaultRetryListener(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void onMessage(RetryRecord retryRecord) {
        start();
        if (retryService == null) {
            throw new EngineException("Retry service not found.");
        }
        retryService.retry(retryRecord);
    }

    @Override
    public void start() {
        RetryExtensionPoint retryExtensionPoint = this.extensionPointRegistry
            .getExtensionPoint(RetryExtensionPoint.class);
        this.retryService = retryExtensionPoint.getExtensionPoint(RetryService.class);
    }

    @Override
    public void stop() {

    }
}
