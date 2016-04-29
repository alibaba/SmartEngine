package com.alibaba.smart.framework.engine.extensibility.exception;

/**
 * ExtensionPointLoadException
 * Created by ettear on 16-4-12.
 */
public class ExtensionPointException extends Exception {

    public ExtensionPointException(String message){
        super(message);
    }

    public ExtensionPointException(String message, Exception e){
        super(message,e);
    }

}
