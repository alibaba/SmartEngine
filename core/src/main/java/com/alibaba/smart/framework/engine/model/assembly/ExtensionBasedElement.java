package com.alibaba.smart.framework.engine.model.assembly;

/**
 * @author pengziran
 * Created by pengziran on 01/08/2017.
 */
public interface ExtensionBasedElement extends IdBasedElement{

    ExtensionElements getExtensionElements();

    void setExtensionElements(ExtensionElements extensionElements);

}
