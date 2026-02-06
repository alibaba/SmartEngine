package com.alibaba.smart.framework.engine.storage;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;

/**
 * Router for selecting storage implementations based on current mode.
 * Supports request-level storage mode selection via ThreadLocal or context.
 *
 * <p>The router resolves storage in the following order:
 * <ol>
 *   <li>Check ThreadLocal (StorageModeHolder) for request-level override</li>
 *   <li>Check StorageContext if provided</li>
 *   <li>Use default mode from configuration</li>
 *   <li>Fall back to DATABASE mode</li>
 * </ol>
 *
 * <p>Example usage:
 * <pre>{@code
 * // Configure router in ProcessEngineConfiguration
 * StorageRouter router = new StorageRouter(config);
 *
 * // Get storage using current mode (from ThreadLocal or default)
 * TaskInstanceStorage storage = router.getStorage(TaskInstanceStorage.class);
 *
 * // Get storage for specific mode
 * TaskInstanceStorage customStorage = router.getStorage(TaskInstanceStorage.class, StorageMode.CUSTOM);
 * }</pre>
 *
 * @author SmartEngine Team
 */
public class StorageRouter {

    private final ProcessEngineConfiguration configuration;
    private final StorageRegistry registry;
    private final List<StorageStrategy> strategies;
    private StorageMode defaultMode = StorageMode.DATABASE;

    public StorageRouter(ProcessEngineConfiguration configuration) {
        this.configuration = configuration;
        this.registry = new StorageRegistry();
        this.strategies = new CopyOnWriteArrayList<>();
    }

    /**
     * Get a storage implementation using the current mode.
     * Mode is resolved from ThreadLocal, then configuration default.
     *
     * @param storageType the storage interface type
     * @param <T>         the storage type
     * @return the storage implementation
     */
    public <T> T getStorage(Class<T> storageType) {
        StorageMode mode = resolveCurrentMode();
        return getStorage(storageType, mode);
    }

    /**
     * Get a storage implementation for a specific mode.
     *
     * @param storageType the storage interface type
     * @param mode        the storage mode
     * @param <T>         the storage type
     * @return the storage implementation
     */
    public <T> T getStorage(Class<T> storageType, StorageMode mode) {
        // First check registry
        T storage = registry.get(mode, storageType);
        if (storage != null) {
            return storage;
        }

        // Try strategies
        StorageContext context = StorageModeHolder.getContext();
        for (StorageStrategy strategy : strategies) {
            if (strategy.getStorageMode() == mode && strategy.supportsStorageType(storageType)) {
                storage = strategy.resolveStorage(storageType, context, configuration);
                if (storage != null) {
                    return storage;
                }
            }
        }

        // No implementation found in registry or strategies
        throw new IllegalStateException(
                "No storage implementation found for type " + storageType.getName() + " and mode " + mode);
    }

    /**
     * Resolve the current storage mode.
     * Checks ThreadLocal first, then context, then default.
     *
     * @return the resolved storage mode
     */
    public StorageMode resolveCurrentMode() {
        // 1. Check ThreadLocal
        StorageMode mode = StorageModeHolder.get();
        if (mode != null) {
            return mode;
        }

        // 2. Check context
        StorageContext context = StorageModeHolder.getContext();
        if (context != null && context.getStorageMode() != null) {
            return context.getStorageMode();
        }

        // 3. Use default
        return defaultMode;
    }

    /**
     * Register a storage implementation.
     *
     * @param mode        the storage mode
     * @param storageType the storage interface type
     * @param instance    the storage implementation
     * @param <T>         the storage type
     */
    public <T> void registerStorage(StorageMode mode, Class<T> storageType, T instance) {
        registry.register(mode, storageType, instance);
    }

    /**
     * Register a storage strategy.
     *
     * @param strategy the strategy to register
     */
    public void registerStrategy(StorageStrategy strategy) {
        strategies.add(strategy);
        // Sort by priority (descending)
        strategies.sort(Comparator.comparingInt(StorageStrategy::getPriority).reversed());
    }

    /**
     * Set the default storage mode.
     *
     * @param defaultMode the default mode
     */
    public void setDefaultMode(StorageMode defaultMode) {
        this.defaultMode = defaultMode;
    }

    /**
     * Get the default storage mode.
     *
     * @return the default mode
     */
    public StorageMode getDefaultMode() {
        return defaultMode;
    }

    /**
     * Get the storage registry.
     *
     * @return the registry
     */
    public StorageRegistry getRegistry() {
        return registry;
    }

    /**
     * Check if a storage type is registered in the router under any mode.
     *
     * @param storageType the storage interface type
     * @return true if registered
     */
    public boolean hasStorageType(Class<?> storageType) {
        return registry.containsType(storageType);
    }
}
