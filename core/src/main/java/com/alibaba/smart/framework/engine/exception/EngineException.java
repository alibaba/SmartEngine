package com.alibaba.smart.framework.engine.exception;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public class EngineException extends RuntimeException {

    private static final long serialVersionUID = -4066455487370828006L;

    public EngineException(String message) {
        super(message);
    }

    public EngineException( Exception e) {
        super(e.getMessage(), e);
    }

    public EngineException(String message, Exception e) {
        super(message, e);
    }

    public EngineException(String message, Throwable e) {
        super(message, e);
    }
}
