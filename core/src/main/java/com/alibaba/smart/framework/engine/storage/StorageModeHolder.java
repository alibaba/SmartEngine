package com.alibaba.smart.framework.engine.storage;

/**
 * ThreadLocal holder for storage mode.
 * Allows request-level storage mode selection via ThreadLocal.
 *
 * <p>Example usage:
 * <pre>{@code
 * StorageModeHolder.set(StorageMode.DATABASE);
 * try {
 *     // All storage operations in this thread will use DATABASE mode
 *     processService.startProcess(request);
 * } finally {
 *     StorageModeHolder.clear();
 * }
 * }</pre>
 *
 * @author SmartEngine Team
 */
public final class StorageModeHolder {

    private static final ThreadLocal<StorageMode> MODE_HOLDER = new ThreadLocal<>();
    private static final ThreadLocal<StorageContext> CONTEXT_HOLDER = new ThreadLocal<>();

    private StorageModeHolder() {
        // Utility class, no instantiation
    }

    /**
     * Set the storage mode for the current thread.
     *
     * @param mode the storage mode to use
     */
    public static void set(StorageMode mode) {
        if (mode == null) {
            MODE_HOLDER.remove();
        } else {
            MODE_HOLDER.set(mode);
        }
    }

    /**
     * Get the storage mode for the current thread.
     *
     * @return the current storage mode, or null if not set
     */
    public static StorageMode get() {
        return MODE_HOLDER.get();
    }

    /**
     * Get the storage mode for the current thread, or return the default if not set.
     *
     * @param defaultMode the default mode to return if not set
     * @return the current storage mode, or the default if not set
     */
    public static StorageMode getOrDefault(StorageMode defaultMode) {
        StorageMode mode = MODE_HOLDER.get();
        return mode != null ? mode : defaultMode;
    }

    /**
     * Clear the storage mode for the current thread.
     * Should be called in a finally block to prevent memory leaks.
     */
    public static void clear() {
        MODE_HOLDER.remove();
    }

    /**
     * Set the storage context for the current thread.
     *
     * @param context the storage context to use
     */
    public static void setContext(StorageContext context) {
        if (context == null) {
            CONTEXT_HOLDER.remove();
        } else {
            CONTEXT_HOLDER.set(context);
            // Also set the mode from context
            if (context.getStorageMode() != null) {
                MODE_HOLDER.set(context.getStorageMode());
            }
        }
    }

    /**
     * Get the storage context for the current thread.
     *
     * @return the current storage context, or null if not set
     */
    public static StorageContext getContext() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * Clear all ThreadLocal values.
     * Should be called in a finally block to prevent memory leaks.
     */
    public static void clearAll() {
        MODE_HOLDER.remove();
        CONTEXT_HOLDER.remove();
    }
}
