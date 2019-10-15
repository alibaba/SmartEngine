package com.alibaba.smart.framework.engine.xml.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.hook.LifeCycleHook;

/**
 * XML处理器扩展点 Created by ettear on 16-4-12.
 */
public interface XmlParserExtensionPoint extends LifeCycleHook {

    /**
     * Reads a model from an XMLStreamReader.
     *
     * @param reader  The XMLStreamReader
     * @param context The context
     * @return A model representation of the input.
     */
    Object parseElement(XMLStreamReader reader, ParseContext context);

    /**
     * Reads a model from an XMLStreamReader.
     *
     * @param reader  The XMLStreamReader
     * @param context The context
     * @return A model representation of the input.
     */
    Object parseAttribute(QName attributeName, XMLStreamReader reader, ParseContext context);

}
