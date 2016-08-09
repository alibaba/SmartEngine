package com.alibaba.smart.framework.engine.extensionpoint.registry.exception;

/**
 * ExtensionPointRegistryException Created by ettear on 16-4-12.
 */
public class ExtensionPointRegistryException extends ExtensionPointException {

    private static final long serialVersionUID = -2345621549560343743L;

    public ExtensionPointRegistryException(String message) {
        super(message);
    }

    public ExtensionPointRegistryException(String message, Exception e) {
        super(message, e);
    }

}
