package com.alibaba.smart.framework.engine.model.assembly;

import java.util.List;
import java.util.Map;

public interface ExtensionElements extends NoneIdBasedElement {

    void decorate(Extension extension);


    List<Extension> getExtensionList();

    //void setExtensionList(List<Extension> extensionList);


    Map<String,Object> getDecorationMap();

    //void   setDecorationMap (Map<String,Object> decorationMap );

}
