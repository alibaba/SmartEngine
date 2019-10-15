package com.alibaba.smart.framework.engine.model.assembly;

/**
 * @author pengziran
 * Created by pengziran on 01/08/2017.
 */
public interface ExtensionBasedElement extends IdBasedElement{
    /**
     * 获取元素ID
     *
     * @return 元素ID
     */

    /**
     * 获取扩展
     * @return Extentions
     */
    ExtensionContainer getExtensionContainer();

    void setExtensionContainer(ExtensionContainer extensionContainer);

}
