package com.alibaba.smart.framework.engine.extensibility.exception;

/**
 * ExtensionPointLoadException
 * Created by ettear on 16-4-12.
 */
public class ExtensionPointLoadException extends Exception {

    public ExtensionPointLoadException(String message){
        super(message);
    }

    public ExtensionPointLoadException(String message,Exception e){
        super(message,e);
    }

}
