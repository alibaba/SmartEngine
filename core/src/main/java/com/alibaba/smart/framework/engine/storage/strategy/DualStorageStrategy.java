package com.alibaba.smart.framework.engine.storage.strategy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.storage.StorageContext;
import com.alibaba.smart.framework.engine.storage.StorageMode;
import com.alibaba.smart.framework.engine.storage.StorageStrategy;

/**
 * Storage strategy for dual-write mode.
 * Writes to both database and custom storage, reads from custom storage for better performance.
 *
 * @author SmartEngine Team
 */
public class DualStorageStrategy implements StorageStrategy {

    private final DatabaseStorageStrategy databaseStrategy;
    private final CustomStorageStrategy customStrategy;
    private final Map<Class<?>, Object> proxyCache = new ConcurrentHashMap<>();

    /**
     * Method name prefixes that indicate write operations.
     */
    private static final Set<String> WRITE_PREFIXES = new HashSet<>(Arrays.asList(
            "insert", "update", "delete", "remove", "save", "create", "add", "close", "mark", "batch"
    ));

    public DualStorageStrategy(DatabaseStorageStrategy databaseStrategy, CustomStorageStrategy customStrategy) {
        this.databaseStrategy = databaseStrategy;
        this.customStrategy = customStrategy;
    }

    @Override
    public StorageMode getStorageMode() {
        return StorageMode.DUAL_WRITE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T resolveStorage(Class<T> storageType, StorageContext context, ProcessEngineConfiguration config) {
        return (T) proxyCache.computeIfAbsent(storageType, type -> createProxy(type, context, config));
    }

    @SuppressWarnings("unchecked")
    private <T> T createProxy(Class<T> storageType, StorageContext context, ProcessEngineConfiguration config) {
        T databaseStorage = databaseStrategy.resolveStorage(storageType, context, config);
        T customStorage = customStrategy.resolveStorage(storageType, context, config);

        if (databaseStorage == null) {
            throw new IllegalStateException("Database storage not found for type: " + storageType.getName());
        }

        // If no custom storage is registered, just return database storage
        if (customStorage == null) {
            return databaseStorage;
        }

        return (T) Proxy.newProxyInstance(
                storageType.getClassLoader(),
                new Class<?>[]{storageType},
                new DualStorageInvocationHandler(databaseStorage, customStorage)
        );
    }

    @Override
    public boolean supportsStorageType(Class<?> storageType) {
        return databaseStrategy.supportsStorageType(storageType);
    }

    @Override
    public int getPriority() {
        return 20; // Higher priority than both database and memory
    }

    /**
     * Invocation handler for dual-write proxy.
     * Routes write operations to both storages, read operations to custom storage first.
     */
    private static class DualStorageInvocationHandler implements InvocationHandler {

        private final Object databaseStorage;
        private final Object customStorage;

        DualStorageInvocationHandler(Object databaseStorage, Object customStorage) {
            this.databaseStorage = databaseStorage;
            this.customStorage = customStorage;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();

            if (isWriteOperation(methodName)) {
                // Write to database first (source of truth)
                Object result = method.invoke(databaseStorage, args);

                // Then sync to memory (best effort)
                try {
                    method.invoke(customStorage, args);
                } catch (Exception e) {
                    // Log but don't fail the operation
                    // In production, this should use proper logging
                    System.err.println("Failed to sync write to custom storage: " + e.getMessage());
                }

                return result;
            } else {
                // Read from memory first, fall back to database
                try {
                    Object result = method.invoke(customStorage, args);
                    if (result != null) {
                        return result;
                    }
                } catch (Exception e) {
                    // Fall through to database
                }
                return method.invoke(databaseStorage, args);
            }
        }

        private boolean isWriteOperation(String methodName) {
            String lowerName = methodName.toLowerCase();
            for (String prefix : WRITE_PREFIXES) {
                if (lowerName.startsWith(prefix)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Clear the proxy cache.
     * Useful when storage configurations change.
     */
    public void clearCache() {
        proxyCache.clear();
    }
}
