package com.alibaba.smart.framework.engine.storage;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;

/**
 * Strategy interface for resolving storage implementations.
 * Implementations can provide different storage resolution logic
 * based on storage mode, context, or other factors.
 *
 * @author SmartEngine Team
 */
public interface StorageStrategy {

    /**
     * Get the storage mode this strategy handles.
     *
     * @return the storage mode
     */
    StorageMode getStorageMode();

    /**
     * Resolve a storage implementation for the given type.
     *
     * @param storageType the storage interface type
     * @param context     the storage context (may be null)
     * @param config      the engine configuration
     * @param <T>         the storage type
     * @return the storage implementation
     */
    <T> T resolveStorage(Class<T> storageType, StorageContext context, ProcessEngineConfiguration config);

    /**
     * Check if this strategy supports the given storage type.
     *
     * @param storageType the storage interface type
     * @return true if supported, false otherwise
     */
    boolean supportsStorageType(Class<?> storageType);

    /**
     * Get the priority of this strategy (higher = more preferred).
     * Used when multiple strategies could handle the same request.
     *
     * @return the priority value
     */
    default int getPriority() {
        return 0;
    }
}
