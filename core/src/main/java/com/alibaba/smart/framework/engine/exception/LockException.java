package com.alibaba.smart.framework.engine.exception;

/**
 * @author 高海军 帝奇 2016.11.11
 */
public class LockException extends EngineException {

    private static final long serialVersionUID = -5576514678329147766L;

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Exception e) {
        super(message, e);
    }
}
