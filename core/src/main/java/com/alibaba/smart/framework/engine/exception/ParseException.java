package com.alibaba.smart.framework.engine.exception;

/**
 * Created by ettear on 16-4-12.
 */
public class ParseException extends EngineException {

    private static final long serialVersionUID = 6304946747125320343L;

    public ParseException( Exception e) {
        super(e.getMessage(), e);
    }


    public ParseException(String message) {
        super(message);
    }
}
