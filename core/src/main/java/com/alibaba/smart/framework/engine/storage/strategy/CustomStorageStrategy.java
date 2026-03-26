package com.alibaba.smart.framework.engine.storage.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.storage.StorageContext;
import com.alibaba.smart.framework.engine.storage.StorageMode;
import com.alibaba.smart.framework.engine.storage.StorageStrategy;

/**
 * Storage strategy for custom mode.
 * Uses pre-registered custom storage implementations.
 *
 * @author SmartEngine Team
 */
public class CustomStorageStrategy implements StorageStrategy {

    private final Map<Class<?>, Object> customStorages = new ConcurrentHashMap<>();

    @Override
    public StorageMode getStorageMode() {
        return StorageMode.CUSTOM;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T resolveStorage(Class<T> storageType, StorageContext context, ProcessEngineConfiguration config) {
        return (T) customStorages.get(storageType);
    }

    @Override
    public boolean supportsStorageType(Class<?> storageType) {
        return customStorages.containsKey(storageType);
    }

    /**
     * Register a custom storage implementation.
     *
     * @param storageType the storage interface type
     * @param instance    the custom storage implementation
     * @param <T>         the storage type
     */
    public <T> void registerCustomStorage(Class<T> storageType, T instance) {
        customStorages.put(storageType, instance);
    }

    /**
     * Remove a custom storage implementation.
     *
     * @param storageType the storage interface type
     * @param <T>         the storage type
     * @return the removed implementation
     */
    @SuppressWarnings("unchecked")
    public <T> T removeCustomStorage(Class<T> storageType) {
        return (T) customStorages.remove(storageType);
    }

    /**
     * Clear all custom storage implementations.
     */
    public void clear() {
        customStorages.clear();
    }

    @Override
    public int getPriority() {
        return 10; // Higher priority than database for custom mode
    }
}
