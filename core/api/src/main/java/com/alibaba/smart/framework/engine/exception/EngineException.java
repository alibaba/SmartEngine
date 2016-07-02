package com.alibaba.smart.framework.engine.exception;

/**
 * Created by ettear on 16-4-12.
 */
public class EngineException extends RuntimeException {

    private static final long serialVersionUID = -4066455487370828006L;

    public EngineException(String message) {
        super(message);
    }

    public EngineException(String message, Exception e) {
        super(message, e);
    }

    public EngineException(String message, Throwable e) {
        super(message, e);
    }
}
