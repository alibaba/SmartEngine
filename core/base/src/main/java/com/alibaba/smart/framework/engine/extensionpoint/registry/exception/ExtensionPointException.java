package com.alibaba.smart.framework.engine.extensionpoint.registry.exception;

/**
 * ExtensionPointLoadException Created by ettear on 16-4-12.
 */
public class ExtensionPointException extends Exception {

    private static final long serialVersionUID = 5691035403298522015L;

    public ExtensionPointException(String message) {
        super(message);
    }

    public ExtensionPointException(String message, Exception e) {
        super(message, e);
    }

}
