package com.alibaba.smart.framework.engine.storage;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for StorageModeHolder.
 *
 * @author SmartEngine Team
 */
public class StorageModeHolderTest {

    @Before
    public void setUp() {
        StorageModeHolder.clearAll();
    }

    @After
    public void tearDown() {
        StorageModeHolder.clearAll();
    }

    @Test
    public void testSetAndGet() {
        Assert.assertNull(StorageModeHolder.get());

        StorageModeHolder.set(StorageMode.DATABASE);
        Assert.assertEquals(StorageMode.DATABASE, StorageModeHolder.get());

        StorageModeHolder.set(StorageMode.CUSTOM);
        Assert.assertEquals(StorageMode.CUSTOM, StorageModeHolder.get());
    }

    @Test
    public void testClear() {
        StorageModeHolder.set(StorageMode.DUAL_WRITE);
        Assert.assertNotNull(StorageModeHolder.get());

        StorageModeHolder.clear();
        Assert.assertNull(StorageModeHolder.get());
    }

    @Test
    public void testSetNull() {
        StorageModeHolder.set(StorageMode.DATABASE);
        Assert.assertNotNull(StorageModeHolder.get());

        StorageModeHolder.set(null);
        Assert.assertNull(StorageModeHolder.get());
    }

    @Test
    public void testGetOrDefault() {
        Assert.assertEquals(StorageMode.DATABASE,
            StorageModeHolder.getOrDefault(StorageMode.DATABASE));

        StorageModeHolder.set(StorageMode.CUSTOM);
        Assert.assertEquals(StorageMode.CUSTOM,
            StorageModeHolder.getOrDefault(StorageMode.DATABASE));
    }

    @Test
    public void testSetContext() {
        StorageContext context = StorageContext.builder()
            .tenantId("tenant001")
            .storageMode(StorageMode.DUAL_WRITE)
            .property("key1", "value1")
            .build();

        StorageModeHolder.setContext(context);

        Assert.assertNotNull(StorageModeHolder.getContext());
        Assert.assertEquals("tenant001", StorageModeHolder.getContext().getTenantId());
        Assert.assertEquals(StorageMode.DUAL_WRITE, StorageModeHolder.getContext().getStorageMode());

        // Mode should also be set from context
        Assert.assertEquals(StorageMode.DUAL_WRITE, StorageModeHolder.get());
    }

    @Test
    public void testSetContextNull() {
        StorageContext context = StorageContext.builder()
            .storageMode(StorageMode.CUSTOM)
            .build();
        StorageModeHolder.setContext(context);
        Assert.assertNotNull(StorageModeHolder.getContext());

        StorageModeHolder.setContext(null);
        Assert.assertNull(StorageModeHolder.getContext());
    }

    @Test
    public void testClearAll() {
        StorageModeHolder.set(StorageMode.DATABASE);
        StorageModeHolder.setContext(StorageContext.builder().tenantId("test").build());

        StorageModeHolder.clearAll();

        Assert.assertNull(StorageModeHolder.get());
        Assert.assertNull(StorageModeHolder.getContext());
    }

    @Test
    public void testThreadIsolation() throws InterruptedException {
        StorageModeHolder.set(StorageMode.DATABASE);

        Thread otherThread = new Thread(() -> {
            // Should be null in other thread
            Assert.assertNull(StorageModeHolder.get());

            StorageModeHolder.set(StorageMode.CUSTOM);
            Assert.assertEquals(StorageMode.CUSTOM, StorageModeHolder.get());
        });

        otherThread.start();
        otherThread.join();

        // Original thread should still have DATABASE
        Assert.assertEquals(StorageMode.DATABASE, StorageModeHolder.get());
    }
}
