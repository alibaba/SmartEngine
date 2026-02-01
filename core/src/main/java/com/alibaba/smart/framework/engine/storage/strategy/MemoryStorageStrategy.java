package com.alibaba.smart.framework.engine.storage.strategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.storage.StorageContext;
import com.alibaba.smart.framework.engine.storage.StorageMode;
import com.alibaba.smart.framework.engine.storage.StorageStrategy;

/**
 * Storage strategy for in-memory mode.
 * Uses pre-registered memory-based storage implementations.
 *
 * @author SmartEngine Team
 */
public class MemoryStorageStrategy implements StorageStrategy {

    private final Map<Class<?>, Object> memoryStorages = new ConcurrentHashMap<>();

    @Override
    public StorageMode getStorageMode() {
        return StorageMode.MEMORY;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T resolveStorage(Class<T> storageType, StorageContext context, ProcessEngineConfiguration config) {
        return (T) memoryStorages.get(storageType);
    }

    @Override
    public boolean supportsStorageType(Class<?> storageType) {
        return memoryStorages.containsKey(storageType);
    }

    /**
     * Register a memory storage implementation.
     *
     * @param storageType the storage interface type
     * @param instance    the memory storage implementation
     * @param <T>         the storage type
     */
    public <T> void registerMemoryStorage(Class<T> storageType, T instance) {
        memoryStorages.put(storageType, instance);
    }

    /**
     * Remove a memory storage implementation.
     *
     * @param storageType the storage interface type
     * @param <T>         the storage type
     * @return the removed implementation
     */
    @SuppressWarnings("unchecked")
    public <T> T removeMemoryStorage(Class<T> storageType) {
        return (T) memoryStorages.remove(storageType);
    }

    /**
     * Clear all memory storage implementations.
     */
    public void clear() {
        memoryStorages.clear();
    }

    @Override
    public int getPriority() {
        return 10; // Higher priority than database for memory mode
    }
}
