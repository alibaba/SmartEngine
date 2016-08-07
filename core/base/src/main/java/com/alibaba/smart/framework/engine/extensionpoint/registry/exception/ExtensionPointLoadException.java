package com.alibaba.smart.framework.engine.extensionpoint.registry.exception;

/**
 * ExtensionPointLoadException Created by ettear on 16-4-12.
 */
public class ExtensionPointLoadException extends ExtensionPointException {

    private static final long serialVersionUID = -2345621549560343743L;

    public ExtensionPointLoadException(String message) {
        super(message);
    }

    public ExtensionPointLoadException(String message, Exception e) {
        super(message, e);
    }

}
