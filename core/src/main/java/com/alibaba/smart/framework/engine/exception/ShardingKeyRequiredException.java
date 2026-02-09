package com.alibaba.smart.framework.engine.exception;

/**
 * Thrown when a query that requires a sharding key (processInstanceId) is invoked
 * without providing one in sharding mode.
 */
public class ShardingKeyRequiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ShardingKeyRequiredException(String message) {
        super(message);
    }
}
