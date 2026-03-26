package com.alibaba.smart.framework.engine.storage;

import com.alibaba.smart.framework.engine.storage.strategy.DatabaseStorageStrategy;
import com.alibaba.smart.framework.engine.storage.strategy.CustomStorageStrategy;
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

    // ============ CustomStorageStrategy Tests ============

    @Test
    public void testCustomStorageStrategyMode() {
        CustomStorageStrategy strategy = new CustomStorageStrategy();
        Assert.assertEquals(StorageMode.CUSTOM, strategy.getStorageMode());
    }

    @Test
    public void testCustomStorageStrategyRegisterAndResolve() {
        CustomStorageStrategy strategy = new CustomStorageStrategy();

        MockStorage mockStorage = () -> "test-value";
        strategy.registerCustomStorage(MockStorage.class, mockStorage);

        Assert.assertTrue(strategy.supportsStorageType(MockStorage.class));

        MockStorage resolved = strategy.resolveStorage(MockStorage.class, null, null);
        Assert.assertSame(mockStorage, resolved);
        Assert.assertEquals("test-value", resolved.getValue());
    }

    @Test
    public void testCustomStorageStrategyNotSupported() {
        CustomStorageStrategy strategy = new CustomStorageStrategy();

        Assert.assertFalse(strategy.supportsStorageType(MockStorage.class));

        MockStorage resolved = strategy.resolveStorage(MockStorage.class, null, null);
        Assert.assertNull(resolved);
    }

    @Test
    public void testCustomStorageStrategyRemove() {
        CustomStorageStrategy strategy = new CustomStorageStrategy();

        MockStorage mockStorage = () -> "value";
        strategy.registerCustomStorage(MockStorage.class, mockStorage);
        Assert.assertTrue(strategy.supportsStorageType(MockStorage.class));

        MockStorage removed = strategy.removeCustomStorage(MockStorage.class);
        Assert.assertSame(mockStorage, removed);
        Assert.assertFalse(strategy.supportsStorageType(MockStorage.class));
    }

    @Test
    public void testCustomStorageStrategyClear() {
        CustomStorageStrategy strategy = new CustomStorageStrategy();

        strategy.registerCustomStorage(MockStorage.class, () -> "value1");
        Assert.assertTrue(strategy.supportsStorageType(MockStorage.class));

        strategy.clear();
        Assert.assertFalse(strategy.supportsStorageType(MockStorage.class));
    }

    @Test
    public void testCustomStorageStrategyPriority() {
        CustomStorageStrategy strategy = new CustomStorageStrategy();
        Assert.assertEquals(10, strategy.getPriority());
    }

    // ============ DualStorageStrategy Tests ============

    @Test
    public void testDualStorageStrategyMode() {
        DatabaseStorageStrategy dbStrategy = new DatabaseStorageStrategy();
        CustomStorageStrategy customStrategy = new CustomStorageStrategy();

        DualStorageStrategy dualStrategy = new DualStorageStrategy(dbStrategy, customStrategy);
        Assert.assertEquals(StorageMode.DUAL_WRITE, dualStrategy.getStorageMode());
    }

    @Test
    public void testDualStorageStrategyPriority() {
        DatabaseStorageStrategy dbStrategy = new DatabaseStorageStrategy();
        CustomStorageStrategy customStrategy = new CustomStorageStrategy();

        DualStorageStrategy dualStrategy = new DualStorageStrategy(dbStrategy, customStrategy);
        Assert.assertEquals(20, dualStrategy.getPriority());
    }

    @Test
    public void testDualStorageStrategySupportsType() {
        DatabaseStorageStrategy dbStrategy = new DatabaseStorageStrategy();
        CustomStorageStrategy customStrategy = new CustomStorageStrategy();

        DualStorageStrategy dualStrategy = new DualStorageStrategy(dbStrategy, customStrategy);

        // Should delegate to database strategy
        Assert.assertTrue(dualStrategy.supportsStorageType(MockStorage.class));
    }

    @Test
    public void testDualStorageStrategyClearCache() {
        DatabaseStorageStrategy dbStrategy = new DatabaseStorageStrategy();
        CustomStorageStrategy customStrategy = new CustomStorageStrategy();

        DualStorageStrategy dualStrategy = new DualStorageStrategy(dbStrategy, customStrategy);

        // Should not throw
        dualStrategy.clearCache();
    }

    // ============ Strategy Priority Ordering ============

    @Test
    public void testStrategyPriorityOrdering() {
        DatabaseStorageStrategy dbStrategy = new DatabaseStorageStrategy();
        CustomStorageStrategy customStrategy = new CustomStorageStrategy();
        DualStorageStrategy dualStrategy = new DualStorageStrategy(dbStrategy, customStrategy);

        // Dual should have highest priority
        Assert.assertTrue(dualStrategy.getPriority() > customStrategy.getPriority());
        Assert.assertTrue(customStrategy.getPriority() > dbStrategy.getPriority());
    }
}
