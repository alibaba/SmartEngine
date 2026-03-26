package com.alibaba.smart.framework.engine.storage;

/**
 * Enumeration of storage modes for SmartEngine.
 * Supports runtime selection of storage implementation per request.
 *
 * @author SmartEngine Team
 */
public enum StorageMode {

    /**
     * Use database storage (default).
     * Data is persisted to relational database.
     */
    DATABASE,

    /**
     * Use custom storage (e.g. in-memory, custom persistence, etc.).
     * Backed by storage-custom module implementations.
     */
    CUSTOM,

    /**
     * Dual-write mode.
     * Writes to both database and memory, reads from memory for performance.
     */
    DUAL_WRITE,

    /**
     * Read from memory, write to database mode.
     * Provides fast reads while ensuring data persistence.
     */
    READ_MEMORY_WRITE_DATABASE
}
