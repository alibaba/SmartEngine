package com.alibaba.smart.framework.engine.storage;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for StorageContext.
 *
 * @author SmartEngine Team
 */
public class StorageContextTest {

    @Test
    public void testBuilder() {
        StorageContext context = StorageContext.builder()
            .tenantId("tenant001")
            .storageMode(StorageMode.DATABASE)
            .build();

        Assert.assertEquals("tenant001", context.getTenantId());
        Assert.assertEquals(StorageMode.DATABASE, context.getStorageMode());
    }

    @Test
    public void testBuilderWithProperties() {
        StorageContext context = StorageContext.builder()
            .tenantId("tenant002")
            .storageMode(StorageMode.CUSTOM)
            .property("key1", "value1")
            .property("key2", 123)
            .property("key3", true)
            .build();

        Assert.assertEquals("tenant002", context.getTenantId());
        Assert.assertEquals(StorageMode.CUSTOM, context.getStorageMode());
        Assert.assertEquals("value1", context.getProperty("key1"));
        Assert.assertEquals(123, context.getProperty("key2"));
        Assert.assertEquals(true, context.getProperty("key3"));
    }

    @Test
    public void testGetPropertyTyped() {
        StorageContext context = StorageContext.builder()
            .property("stringKey", "stringValue")
            .property("intKey", 42)
            .property("boolKey", false)
            .build();

        String strVal = context.getProperty("stringKey", String.class);
        Integer intVal = context.getProperty("intKey", Integer.class);
        Boolean boolVal = context.getProperty("boolKey", Boolean.class);

        Assert.assertEquals("stringValue", strVal);
        Assert.assertEquals(Integer.valueOf(42), intVal);
        Assert.assertEquals(Boolean.FALSE, boolVal);
    }

    @Test
    public void testGetPropertyNull() {
        StorageContext context = StorageContext.builder().build();

        Assert.assertNull(context.getProperty("nonexistent"));
        Assert.assertNull(context.getProperty("nonexistent", String.class));
    }

    @Test
    public void testGetProperties() {
        StorageContext context = StorageContext.builder()
            .property("a", 1)
            .property("b", 2)
            .build();

        Assert.assertEquals(2, context.getProperties().size());
        Assert.assertTrue(context.getProperties().containsKey("a"));
        Assert.assertTrue(context.getProperties().containsKey("b"));
    }

    @Test
    public void testEmptyContext() {
        StorageContext context = StorageContext.builder().build();

        Assert.assertNull(context.getTenantId());
        Assert.assertNull(context.getStorageMode());
        Assert.assertTrue(context.getProperties().isEmpty());
    }

    @Test
    public void testToString() {
        StorageContext context = StorageContext.builder()
            .tenantId("tenant001")
            .storageMode(StorageMode.DUAL_WRITE)
            .build();

        String str = context.toString();
        Assert.assertTrue(str.contains("tenant001"));
        Assert.assertTrue(str.contains("DUAL_WRITE"));
    }
}
