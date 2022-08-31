package com.alibaba.smart.framework.engine.model.assembly;

import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

import java.util.List;
import java.util.Map;

public interface ExtensionElements extends NoneIdBasedElement {

    void decorate(ExtensionDecorator extension, ParseContext context);


    List<ExtensionDecorator> getExtensionList();

    //void setExtensionList(List<Extension> extensionList);


    Map<String,Object> getDecorationMap();

    //void   setDecorationMap (Map<String,Object> decorationMap );

}
