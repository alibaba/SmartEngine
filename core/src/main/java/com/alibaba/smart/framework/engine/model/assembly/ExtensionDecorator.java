package com.alibaba.smart.framework.engine.model.assembly;

import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */

public interface ExtensionDecorator extends NoneIdBasedElement {

    //Object getKey();

    String getDecoratorType();

    void decorate(ExtensionElements extensionElements, ParseContext context);

}
