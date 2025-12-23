package com.alibaba.smart.framework.engine.exception;

/**
 * 流程回退相关异常
 * 
 * @author SmartEngine Team
 */
public class RollbackException extends EngineException {

    private static final long serialVersionUID = -2066455487370828007L;

    public RollbackException(String message) {
        super(message);
    }

    public RollbackException(Exception e) {
        super(e);
    }

    public RollbackException(String message, Exception e) {
        super(message, e);
    }

    public RollbackException(String message, Throwable e) {
        super(message, e);
    }
}