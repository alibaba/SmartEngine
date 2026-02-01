package com.alibaba.smart.framework.engine.storage.strategy;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.storage.StorageContext;
import com.alibaba.smart.framework.engine.storage.StorageMode;
import com.alibaba.smart.framework.engine.storage.StorageStrategy;

/**
 * Storage strategy for database mode.
 * Resolves storage implementations from AnnotationScanner (existing mechanism).
 *
 * @author SmartEngine Team
 */
public class DatabaseStorageStrategy implements StorageStrategy {

    @Override
    public StorageMode getStorageMode() {
        return StorageMode.DATABASE;
    }

    @Override
    public <T> T resolveStorage(Class<T> storageType, StorageContext context, ProcessEngineConfiguration config) {
        AnnotationScanner scanner = config.getAnnotationScanner();
        if (scanner != null) {
            return scanner.getExtensionPoint(ExtensionConstant.COMMON, storageType);
        }
        return null;
    }

    @Override
    public boolean supportsStorageType(Class<?> storageType) {
        // Database strategy supports all storage types
        return true;
    }

    @Override
    public int getPriority() {
        // Default priority
        return 0;
    }
}
