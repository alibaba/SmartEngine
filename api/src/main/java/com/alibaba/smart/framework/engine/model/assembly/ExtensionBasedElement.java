package com.alibaba.smart.framework.engine.model.assembly;

/**
 * @author pengziran
 * Created by pengziran on 01/08/2017.
 */
public interface ExtensionBasedElement extends IdBasedElement{

    ExtensionContainer getExtensionContainer();

    void setExtensionContainer(ExtensionContainer extensionContainer);

}
