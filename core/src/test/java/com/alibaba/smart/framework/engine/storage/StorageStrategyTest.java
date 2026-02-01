package com.alibaba.smart.framework.engine.storage;

import com.alibaba.smart.framework.engine.storage.strategy.DatabaseStorageStrategy;
import com.alibaba.smart.framework.engine.storage.strategy.MemoryStorageStrategy;
import com.alibaba.smart.framework.engine.storage.strategy.DualStorageStrategy;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for StorageStrategy implementations.
 *
 * @author SmartEngine Team
 */
public class StorageStrategyTest {

    // Mock storage interface for testing
    interface MockStorage {
        String getValue();
    }

    // ============ DatabaseStorageStrategy Tests ============

    @Test
    public void testDatabaseStorageStrategyMode() {
        DatabaseStorageStrategy strategy = new DatabaseStorageStrategy();
        Assert.assertEquals(StorageMode.DATABASE, strategy.getStorageMode());
    }

    @Test
    public void testDatabaseStorageStrategySupportsAllTypes() {
        DatabaseStorageStrategy strategy = new DatabaseStorageStrategy();

        Assert.assertTrue(strategy.supportsStorageType(MockStorage.class));
        Assert.assertTrue(strategy.supportsStorageType(String.class));
        Assert.assertTrue(strategy.supportsStorageType(Object.class));
    }

    @Test
    public void testDatabaseStorageStrategyPriority() {
        DatabaseStorageStrategy strategy = new DatabaseStorageStrategy();
        Assert.assertEquals(0, strategy.getPriority());
    }

    // ============ MemoryStorageStrategy Tests ============

    @Test
    public void testMemoryStorageStrategyMode() {
        MemoryStorageStrategy strategy = new MemoryStorageStrategy();
        Assert.assertEquals(StorageMode.MEMORY, strategy.getStorageMode());
    }

    @Test
    public void testMemoryStorageStrategyRegisterAndResolve() {
        MemoryStorageStrategy strategy = new MemoryStorageStrategy();

        MockStorage mockStorage = () -> "test-value";
        strategy.registerMemoryStorage(MockStorage.class, mockStorage);

        Assert.assertTrue(strategy.supportsStorageType(MockStorage.class));

        MockStorage resolved = strategy.resolveStorage(MockStorage.class, null, null);
        Assert.assertSame(mockStorage, resolved);
        Assert.assertEquals("test-value", resolved.getValue());
    }

    @Test
    public void testMemoryStorageStrategyNotSupported() {
        MemoryStorageStrategy strategy = new MemoryStorageStrategy();

        Assert.assertFalse(strategy.supportsStorageType(MockStorage.class));

        MockStorage resolved = strategy.resolveStorage(MockStorage.class, null, null);
        Assert.assertNull(resolved);
    }

    @Test
    public void testMemoryStorageStrategyRemove() {
        MemoryStorageStrategy strategy = new MemoryStorageStrategy();

        MockStorage mockStorage = () -> "value";
        strategy.registerMemoryStorage(MockStorage.class, mockStorage);
        Assert.assertTrue(strategy.supportsStorageType(MockStorage.class));

        MockStorage removed = strategy.removeMemoryStorage(MockStorage.class);
        Assert.assertSame(mockStorage, removed);
        Assert.assertFalse(strategy.supportsStorageType(MockStorage.class));
    }

    @Test
    public void testMemoryStorageStrategyClear() {
        MemoryStorageStrategy strategy = new MemoryStorageStrategy();

        strategy.registerMemoryStorage(MockStorage.class, () -> "value1");
        Assert.assertTrue(strategy.supportsStorageType(MockStorage.class));

        strategy.clear();
        Assert.assertFalse(strategy.supportsStorageType(MockStorage.class));
    }

    @Test
    public void testMemoryStorageStrategyPriority() {
        MemoryStorageStrategy strategy = new MemoryStorageStrategy();
        Assert.assertEquals(10, strategy.getPriority());
    }

    // ============ DualStorageStrategy Tests ============

    @Test
    public void testDualStorageStrategyMode() {
        DatabaseStorageStrategy dbStrategy = new DatabaseStorageStrategy();
        MemoryStorageStrategy memStrategy = new MemoryStorageStrategy();

        DualStorageStrategy dualStrategy = new DualStorageStrategy(dbStrategy, memStrategy);
        Assert.assertEquals(StorageMode.DUAL_WRITE, dualStrategy.getStorageMode());
    }

    @Test
    public void testDualStorageStrategyPriority() {
        DatabaseStorageStrategy dbStrategy = new DatabaseStorageStrategy();
        MemoryStorageStrategy memStrategy = new MemoryStorageStrategy();

        DualStorageStrategy dualStrategy = new DualStorageStrategy(dbStrategy, memStrategy);
        Assert.assertEquals(20, dualStrategy.getPriority());
    }

    @Test
    public void testDualStorageStrategySupportsType() {
        DatabaseStorageStrategy dbStrategy = new DatabaseStorageStrategy();
        MemoryStorageStrategy memStrategy = new MemoryStorageStrategy();

        DualStorageStrategy dualStrategy = new DualStorageStrategy(dbStrategy, memStrategy);

        // Should delegate to database strategy
        Assert.assertTrue(dualStrategy.supportsStorageType(MockStorage.class));
    }

    @Test
    public void testDualStorageStrategyClearCache() {
        DatabaseStorageStrategy dbStrategy = new DatabaseStorageStrategy();
        MemoryStorageStrategy memStrategy = new MemoryStorageStrategy();

        DualStorageStrategy dualStrategy = new DualStorageStrategy(dbStrategy, memStrategy);

        // Should not throw
        dualStrategy.clearCache();
    }

    // ============ Strategy Priority Ordering ============

    @Test
    public void testStrategyPriorityOrdering() {
        DatabaseStorageStrategy dbStrategy = new DatabaseStorageStrategy();
        MemoryStorageStrategy memStrategy = new MemoryStorageStrategy();
        DualStorageStrategy dualStrategy = new DualStorageStrategy(dbStrategy, memStrategy);

        // Dual should have highest priority
        Assert.assertTrue(dualStrategy.getPriority() > memStrategy.getPriority());
        Assert.assertTrue(memStrategy.getPriority() > dbStrategy.getPriority());
    }
}
