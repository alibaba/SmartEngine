package com.alibaba.smart.framework.engine.exception;

/**
 * 引擎异常 Created by ettear on 16-4-12.
 */
public class EngineException extends RuntimeException {

    private static final long serialVersionUID = -4066455487370828006L;

    public EngineException(String message) {
        super(message);
    }

    public EngineException(String message, Exception e) {
        super(message, e);
    }
}
