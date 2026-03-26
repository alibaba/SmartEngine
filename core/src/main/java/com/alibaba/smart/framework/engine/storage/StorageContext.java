package com.alibaba.smart.framework.engine.storage;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Context information for storage operations.
 * Can be used to pass request-level storage preferences.
 *
 * @author SmartEngine Team
 */
public class StorageContext implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The tenant ID for multi-tenant scenarios
     */
    private String tenantId;

    /**
     * The storage mode to use for this request
     */
    private StorageMode storageMode;

    /**
     * Additional context properties
     */
    private Map<String, Object> properties;

    private StorageContext() {
        this.properties = new HashMap<>();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getTenantId() {
        return tenantId;
    }

    public StorageMode getStorageMode() {
        return storageMode;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getProperty(String key, Class<T> type) {
        Object value = properties.get(key);
        if (value == null) {
            return null;
        }
        return (T) value;
    }

    public static class Builder {
        private final StorageContext context;

        private Builder() {
            this.context = new StorageContext();
        }

        public Builder tenantId(String tenantId) {
            context.tenantId = tenantId;
            return this;
        }

        public Builder storageMode(StorageMode storageMode) {
            context.storageMode = storageMode;
            return this;
        }

        public Builder property(String key, Object value) {
            context.properties.put(key, value);
            return this;
        }

        public StorageContext build() {
            return context;
        }
    }

    @Override
    public String toString() {
        return "StorageContext{" +
                "tenantId='" + tenantId + '\'' +
                ", storageMode=" + storageMode +
                ", properties=" + properties +
                '}';
    }
}
