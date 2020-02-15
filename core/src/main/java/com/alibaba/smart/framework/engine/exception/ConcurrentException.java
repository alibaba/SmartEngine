package com.alibaba.smart.framework.engine.exception;

/**
 * @author zaimang.tj
 * @date 2017/11/8
 */
public class ConcurrentException extends EngineException {
    private static final long serialVersionUID = -5576514534329147766L;

    public ConcurrentException(String message) {
        super(message);
    }

    public ConcurrentException(Exception e) {
        super(e);
    }

    public ConcurrentException(String message, Exception e) {
        super(message, e);
    }

    public ConcurrentException(String message, Throwable e) {
        super(message, e);
    }
}
