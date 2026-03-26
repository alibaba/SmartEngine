package com.alibaba.smart.framework.engine.configuration.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for SnowflakeIdGenerator.
 */
public class SnowflakeIdGeneratorTest {

    @Test
    public void testBasicGeneration() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(0);
        long id = generator.nextId();
        Assert.assertTrue("ID should be positive", id > 0);
    }

    @Test
    public void testMonotonicallyIncreasing() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(0);
        long prev = 0;
        for (int i = 0; i < 10000; i++) {
            long id = generator.nextId();
            Assert.assertTrue("IDs should be monotonically increasing: prev=" + prev + " current=" + id, id > prev);
            prev = id;
        }
    }

    @Test
    public void testUniqueness() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(0);
        Set<Long> ids = new HashSet<Long>();
        int count = 100000;
        for (int i = 0; i < count; i++) {
            long id = generator.nextId();
            Assert.assertTrue("Duplicate ID detected: " + id, ids.add(id));
        }
        Assert.assertEquals(count, ids.size());
    }

    @Test
    public void testParseTimestamp() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(0);
        long beforeMs = System.currentTimeMillis();
        long id = generator.nextId();
        long afterMs = System.currentTimeMillis();

        long parsedTimestamp = SnowflakeIdGenerator.parseTimestamp(id);
        Assert.assertTrue("Parsed timestamp should be >= beforeMs",
                parsedTimestamp >= beforeMs);
        Assert.assertTrue("Parsed timestamp should be <= afterMs",
                parsedTimestamp <= afterMs);
    }

    @Test
    public void testParseNodeId() {
        for (int nodeId = 0; nodeId < 32; nodeId++) {
            SnowflakeIdGenerator generator = new SnowflakeIdGenerator(nodeId);
            long id = generator.nextId();
            Assert.assertEquals("Node ID should match", nodeId, SnowflakeIdGenerator.parseNodeId(id));
        }
    }

    @Test
    public void testParseSequence() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(0);
        long id = generator.nextId();
        long seq = SnowflakeIdGenerator.parseSequence(id);
        Assert.assertTrue("Sequence should be >= 0", seq >= 0);
        Assert.assertTrue("Sequence should be <= 131071", seq <= 131071);
    }

    @Test
    public void testDifferentNodesProduceDifferentIds() {
        SnowflakeIdGenerator gen0 = new SnowflakeIdGenerator(0);
        SnowflakeIdGenerator gen1 = new SnowflakeIdGenerator(1);

        long id0 = gen0.nextId();
        long id1 = gen1.nextId();

        Assert.assertNotEquals("Different nodes should produce different IDs", id0, id1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNodeIdNegative() {
        new SnowflakeIdGenerator(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNodeIdTooLarge() {
        new SnowflakeIdGenerator(32);
    }

    @Test
    public void testDefaultConstructor() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator();
        long id = generator.nextId();
        Assert.assertEquals("Default node ID should be 0", 0, SnowflakeIdGenerator.parseNodeId(id));
    }

    @Test
    public void testGetIdType() {
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator();
        Assert.assertEquals(Long.class, generator.getIdType());
    }

    @Test
    public void testConcurrentUniqueness() throws InterruptedException {
        final SnowflakeIdGenerator generator = new SnowflakeIdGenerator(0);
        final int threadCount = 8;
        final int idsPerThread = 10000;
        final Set<Long> allIds = ConcurrentHashMap.newKeySet();
        final AtomicBoolean hasDuplicate = new AtomicBoolean(false);
        final CountDownLatch latch = new CountDownLatch(threadCount);

        for (int t = 0; t < threadCount; t++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < idsPerThread; i++) {
                            long id = generator.nextId();
                            if (!allIds.add(id)) {
                                hasDuplicate.set(true);
                            }
                        }
                    } finally {
                        latch.countDown();
                    }
                }
            }).start();
        }

        latch.await();
        Assert.assertFalse("No duplicate IDs should exist across threads", hasDuplicate.get());
        Assert.assertEquals("Total unique IDs should match",
                threadCount * idsPerThread, allIds.size());
    }

    @Test
    public void testSequenceWraparound() {
        // Generate many IDs in quick succession to trigger sequence increment
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(0);
        long prev = 0;
        for (int i = 0; i < 200000; i++) {
            long id = generator.nextId();
            Assert.assertTrue("IDs should remain monotonically increasing during sequence wrap",
                    id > prev);
            prev = id;
        }
    }

    @Test
    public void testClockRollbackSmall() {
        // Create a generator that simulates 3ms clock rollback then recovers
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(0) {
            private int callCount = 0;
            @Override
            long currentTimeMillis() {
                callCount++;
                if (callCount == 2) {
                    // Second call simulates 3ms rollback
                    return System.currentTimeMillis() - 3;
                }
                return System.currentTimeMillis();
            }
        };

        // First call establishes lastTimestamp
        long id1 = generator.nextId();
        // Second call triggers rollback handling (should wait and recover)
        long id2 = generator.nextId();

        Assert.assertTrue("Should still generate valid IDs after small rollback", id2 > 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testClockRollbackLarge() {
        // Create a generator that simulates large clock rollback
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(0) {
            private int callCount = 0;
            @Override
            long currentTimeMillis() {
                callCount++;
                if (callCount >= 2) {
                    // All subsequent calls return a time 10ms in the past
                    return super.currentTimeMillis() - 100;
                }
                return super.currentTimeMillis();
            }
        };

        generator.nextId(); // Establishes lastTimestamp
        generator.nextId(); // Should throw IllegalStateException
    }
}
