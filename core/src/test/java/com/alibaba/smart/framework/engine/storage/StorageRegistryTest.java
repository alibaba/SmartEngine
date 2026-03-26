package com.alibaba.smart.framework.engine.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for StorageRegistry.
 *
 * @author SmartEngine Team
 */
public class StorageRegistryTest {

    private StorageRegistry registry;

    // Mock storage interface for testing
    interface MockStorage {
        String getData();
    }

    // Mock implementation
    static class MockDatabaseStorage implements MockStorage {
        @Override
        public String getData() {
            return "database";
        }
    }

    static class MockMemoryStorage implements MockStorage {
        @Override
        public String getData() {
            return "memory";
        }
    }

    @Before
    public void setUp() {
        registry = new StorageRegistry();
    }

    @Test
    public void testRegisterAndGet() {
        MockStorage dbStorage = new MockDatabaseStorage();
        MockStorage memStorage = new MockMemoryStorage();

        registry.register(StorageMode.DATABASE, MockStorage.class, dbStorage);
        registry.register(StorageMode.CUSTOM, MockStorage.class, memStorage);

        MockStorage retrieved1 = registry.get(StorageMode.DATABASE, MockStorage.class);
        MockStorage retrieved2 = registry.get(StorageMode.CUSTOM, MockStorage.class);

        Assert.assertSame(dbStorage, retrieved1);
        Assert.assertSame(memStorage, retrieved2);
    }

    @Test
    public void testGetNotFound() {
        MockStorage result = registry.get(StorageMode.DATABASE, MockStorage.class);
        Assert.assertNull(result);
    }

    @Test
    public void testContains() {
        Assert.assertFalse(registry.contains(StorageMode.DATABASE, MockStorage.class));

        registry.register(StorageMode.DATABASE, MockStorage.class, new MockDatabaseStorage());

        Assert.assertTrue(registry.contains(StorageMode.DATABASE, MockStorage.class));
        Assert.assertFalse(registry.contains(StorageMode.CUSTOM, MockStorage.class));
    }

    @Test
    public void testRemove() {
        MockStorage storage = new MockDatabaseStorage();
        registry.register(StorageMode.DATABASE, MockStorage.class, storage);

        Assert.assertTrue(registry.contains(StorageMode.DATABASE, MockStorage.class));

        MockStorage removed = registry.remove(StorageMode.DATABASE, MockStorage.class);

        Assert.assertSame(storage, removed);
        Assert.assertFalse(registry.contains(StorageMode.DATABASE, MockStorage.class));
    }

    @Test
    public void testRemoveNotFound() {
        MockStorage removed = registry.remove(StorageMode.DATABASE, MockStorage.class);
        Assert.assertNull(removed);
    }

    @Test
    public void testClear() {
        registry.register(StorageMode.DATABASE, MockStorage.class, new MockDatabaseStorage());
        registry.register(StorageMode.CUSTOM, MockStorage.class, new MockMemoryStorage());

        Assert.assertTrue(registry.contains(StorageMode.DATABASE, MockStorage.class));
        Assert.assertTrue(registry.contains(StorageMode.CUSTOM, MockStorage.class));

        registry.clear();

        Assert.assertFalse(registry.contains(StorageMode.DATABASE, MockStorage.class));
        Assert.assertFalse(registry.contains(StorageMode.CUSTOM, MockStorage.class));
    }

    @Test
    public void testClearByMode() {
        registry.register(StorageMode.DATABASE, MockStorage.class, new MockDatabaseStorage());
        registry.register(StorageMode.CUSTOM, MockStorage.class, new MockMemoryStorage());

        registry.clear(StorageMode.DATABASE);

        Assert.assertFalse(registry.contains(StorageMode.DATABASE, MockStorage.class));
        Assert.assertTrue(registry.contains(StorageMode.CUSTOM, MockStorage.class));
    }

    // Another mock storage interface for testing multiple types
    interface AnotherStorage {
        int getCount();
    }

    static class AnotherStorageImpl implements AnotherStorage {
        @Override
        public int getCount() {
            return 42;
        }
    }

    @Test
    public void testMultipleStorageTypes() {
        registry.register(StorageMode.DATABASE, MockStorage.class, new MockDatabaseStorage());
        registry.register(StorageMode.DATABASE, AnotherStorage.class, new AnotherStorageImpl());

        MockStorage mock = registry.get(StorageMode.DATABASE, MockStorage.class);
        AnotherStorage another = registry.get(StorageMode.DATABASE, AnotherStorage.class);

        Assert.assertNotNull(mock);
        Assert.assertNotNull(another);
        Assert.assertEquals("database", mock.getData());
        Assert.assertEquals(42, another.getCount());
    }
}
