package com.alibaba.smart.framework.engine.configuration.impl;

import com.alibaba.smart.framework.engine.configuration.IdGenerator;
import com.alibaba.smart.framework.engine.model.instance.Instance;

/**
 * Snowflake-based distributed ID generator.
 *
 * <p>Bit layout (64 bits total):
 * <pre>
 *  0                   1                   2                   3
 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |0|                    timestamp (41 bits)                        |
 * +-+                               +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * |                                 | nodeId(5) |  sequence (17 bits)|
 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
 * </pre>
 *
 * <ul>
 *   <li>1 bit  - sign (always 0, positive)</li>
 *   <li>41 bits - millisecond timestamp offset from custom epoch (supports ~69 years)</li>
 *   <li>5 bits  - node ID (0-31, supports 32 nodes)</li>
 *   <li>17 bits - sequence number per millisecond (0-131071, supports 131072 IDs/ms/node)</li>
 * </ul>
 *
 * <p>Custom epoch: 2024-01-01 00:00:00 UTC (1704067200000L)
 *
 * <p>Clock rollback protection: tolerates up to 5ms of clock drift by busy-waiting;
 * throws {@link IllegalStateException} for larger rollbacks.
 *
 * <p>Thread safety: all mutable state is protected by {@code synchronized}.
 */
public class SnowflakeIdGenerator implements IdGenerator {

    // Custom epoch: 2024-01-01 00:00:00 UTC
    private static final long EPOCH = 1704067200000L;

    // Bit lengths
    private static final int NODE_ID_BITS = 5;
    private static final int SEQUENCE_BITS = 17;

    // Max values
    private static final long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1;       // 31
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;     // 131071

    // Shift amounts
    private static final int NODE_ID_SHIFT = SEQUENCE_BITS;                 // 17
    private static final int TIMESTAMP_SHIFT = NODE_ID_BITS + SEQUENCE_BITS; // 22

    // Maximum tolerable clock rollback in milliseconds
    private static final long MAX_CLOCK_BACKWARD_MS = 5L;

    private final long nodeId;
    private long lastTimestamp = -1L;
    private long sequence = 0L;

    /**
     * Create a SnowflakeIdGenerator with the specified node ID.
     *
     * @param nodeId node identifier, must be between 0 and 31 (inclusive)
     * @throws IllegalArgumentException if nodeId is out of range
     */
    public SnowflakeIdGenerator(long nodeId) {
        if (nodeId < 0 || nodeId > MAX_NODE_ID) {
            throw new IllegalArgumentException(
                    "nodeId must be between 0 and " + MAX_NODE_ID + ", got: " + nodeId);
        }
        this.nodeId = nodeId;
    }

    /**
     * Create a SnowflakeIdGenerator with default node ID 0.
     */
    public SnowflakeIdGenerator() {
        this(0);
    }

    @Override
    public Class<?> getIdType() {
        return Long.class;
    }

    @Override
    public void generate(Instance instance) {
        long id = nextId();
        instance.setInstanceId(String.valueOf(id));
    }

    /**
     * Generate the next unique snowflake ID.
     *
     * @return a unique 64-bit snowflake ID
     * @throws IllegalStateException if clock moved backward by more than 5ms
     */
    public synchronized long nextId() {
        long currentTimestamp = currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            long offset = lastTimestamp - currentTimestamp;
            if (offset <= MAX_CLOCK_BACKWARD_MS) {
                // Wait for the clock to catch up
                try {
                    Thread.sleep(offset);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(
                            "Interrupted while waiting for clock to catch up", e);
                }
                currentTimestamp = currentTimeMillis();
                if (currentTimestamp < lastTimestamp) {
                    throw new IllegalStateException(
                            "Clock moved backward by " + (lastTimestamp - currentTimestamp)
                                    + "ms after waiting. Refusing to generate ID.");
                }
            } else {
                throw new IllegalStateException(
                        "Clock moved backward by " + offset
                                + "ms (exceeds tolerance of " + MAX_CLOCK_BACKWARD_MS + "ms). "
                                + "Refusing to generate ID.");
            }
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // Sequence exhausted for this millisecond, wait for next ms
                currentTimestamp = waitForNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        long timestampOffset = currentTimestamp - EPOCH;
        return (timestampOffset << TIMESTAMP_SHIFT)
                | (nodeId << NODE_ID_SHIFT)
                | sequence;
    }

    /**
     * Parse the timestamp (millis since Unix epoch) from a snowflake ID.
     *
     * @param id the snowflake ID
     * @return the absolute timestamp in milliseconds (Unix epoch)
     */
    public static long parseTimestamp(long id) {
        return (id >> TIMESTAMP_SHIFT) + EPOCH;
    }

    /**
     * Parse the node ID from a snowflake ID.
     *
     * @param id the snowflake ID
     * @return the node ID (0-31)
     */
    public static long parseNodeId(long id) {
        return (id >> NODE_ID_SHIFT) & MAX_NODE_ID;
    }

    /**
     * Parse the sequence number from a snowflake ID.
     *
     * @param id the snowflake ID
     * @return the sequence number (0-131071)
     */
    public static long parseSequence(long id) {
        return id & MAX_SEQUENCE;
    }

    /**
     * Block until the system clock advances past the given timestamp.
     */
    private long waitForNextMillis(long lastTs) {
        long ts = currentTimeMillis();
        while (ts <= lastTs) {
            ts = currentTimeMillis();
        }
        return ts;
    }

    /**
     * Returns current time in milliseconds. Extracted for testability.
     */
    long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
