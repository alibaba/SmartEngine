package com.alibaba.smart.framework.engine.exception;

public class ValidationException extends EngineException {


    public ValidationException(Exception e) {
        super(e.getMessage(), e);
    }


    public ValidationException(String message) {
        super(message);
    }
}
