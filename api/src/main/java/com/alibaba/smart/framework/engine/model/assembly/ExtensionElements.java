package com.alibaba.smart.framework.engine.model.assembly;

public interface ExtensionElements extends NoneIdBasedElement {

    void decorate(Extension extension);

    Object getExtension(String key);

    //void

}
