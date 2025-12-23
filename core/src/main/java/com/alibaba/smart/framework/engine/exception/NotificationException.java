package com.alibaba.smart.framework.engine.exception;

/**
 * 知会通知相关异常
 * 
 * @author SmartEngine Team
 */
public class NotificationException extends EngineException {

    private static final long serialVersionUID = 1L;

    public NotificationException(String message) {
        super(message);
    }

    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }


}