package com.alibaba.smart.framework.engine.storage;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry for storage implementations by mode.
 * Maintains a mapping from StorageMode and storage type to actual implementation.
 *
 * @author SmartEngine Team
 */
public class StorageRegistry {

    /**
     * Storage implementations by mode and type
     * Map<StorageMode, Map<Class<?>, Object>>
     */
    private final Map<StorageMode, Map<Class<?>, Object>> storageMap;

    public StorageRegistry() {
        this.storageMap = new EnumMap<>(StorageMode.class);
        for (StorageMode mode : StorageMode.values()) {
            storageMap.put(mode, new HashMap<>());
        }
    }

    /**
     * Register a storage implementation for a specific mode and type.
     *
     * @param mode        the storage mode
     * @param storageType the storage interface type
     * @param instance    the storage implementation instance
     * @param <T>         the storage type
     */
    public <T> void register(StorageMode mode, Class<T> storageType, T instance) {
        storageMap.get(mode).put(storageType, instance);
    }

    /**
     * Get a storage implementation for a specific mode and type.
     *
     * @param mode        the storage mode
     * @param storageType the storage interface type
     * @param <T>         the storage type
     * @return the storage implementation, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T get(StorageMode mode, Class<T> storageType) {
        Map<Class<?>, Object> modeMap = storageMap.get(mode);
        if (modeMap == null) {
            return null;
        }
        return (T) modeMap.get(storageType);
    }

    /**
     * Check if a storage implementation is registered for a specific mode and type.
     *
     * @param mode        the storage mode
     * @param storageType the storage interface type
     * @return true if registered, false otherwise
     */
    public boolean contains(StorageMode mode, Class<?> storageType) {
        Map<Class<?>, Object> modeMap = storageMap.get(mode);
        return modeMap != null && modeMap.containsKey(storageType);
    }

    /**
     * Remove a storage implementation for a specific mode and type.
     *
     * @param mode        the storage mode
     * @param storageType the storage interface type
     * @param <T>         the storage type
     * @return the removed implementation, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> T remove(StorageMode mode, Class<T> storageType) {
        Map<Class<?>, Object> modeMap = storageMap.get(mode);
        if (modeMap == null) {
            return null;
        }
        return (T) modeMap.remove(storageType);
    }

    /**
     * Clear all storage implementations.
     */
    public void clear() {
        for (Map<Class<?>, Object> modeMap : storageMap.values()) {
            modeMap.clear();
        }
    }

    /**
     * Clear all storage implementations for a specific mode.
     *
     * @param mode the storage mode
     */
    public void clear(StorageMode mode) {
        Map<Class<?>, Object> modeMap = storageMap.get(mode);
        if (modeMap != null) {
            modeMap.clear();
        }
    }

    /**
     * Check if a storage type is registered under any mode.
     *
     * @param storageType the storage interface type
     * @return true if registered under at least one mode
     */
    public boolean containsType(Class<?> storageType) {
        for (Map<Class<?>, Object> modeMap : storageMap.values()) {
            if (modeMap.containsKey(storageType)) {
                return true;
            }
        }
        return false;
    }
}
