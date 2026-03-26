package com.alibaba.smart.framework.engine.exception;

/**
 * 督办相关异常
 * 
 * @author SmartEngine Team
 */
public class SupervisionException extends EngineException {

    private static final long serialVersionUID = 1L;

    public SupervisionException(String message) {
        super(message);
    }

    public SupervisionException(String message, Throwable cause) {
        super(message, cause);
    }


}