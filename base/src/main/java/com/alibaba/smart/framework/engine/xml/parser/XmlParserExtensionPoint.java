package com.alibaba.smart.framework.engine.xml.parser;

import com.alibaba.smart.framework.engine.hook.LifeCycleHook;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

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
    Object parseElement(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException;

    /**
     * Reads a model from an XMLStreamReader.
     *
     * @param reader  The XMLStreamReader
     * @param context The context
     * @return A model representation of the input.
     */
    Object parseAttribute(QName attributeName, XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException;

}
