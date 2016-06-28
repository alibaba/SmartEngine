package com.alibaba.smart.framework.engine.assembly.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ResolveException;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;

/**
 * XML处理器扩展点 Created by ettear on 16-4-12.
 */
public interface AssemblyParserExtensionPoint extends LifeCycleListener {

    /**
     * Reads a model from an XMLStreamReader.
     *
     * @param reader The XMLStreamReader
     * @param context The context
     * @return A model representation of the input.
     */
    Object parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException;

    /**
     * Reads a model from an XMLStreamReader.
     *
     * @param reader The XMLStreamReader
     * @param context The context
     * @return A model representation of the input.
     */
    Object readAttribute(QName attributeName, XMLStreamReader reader, ParseContext context) throws ParseException,
                                                                                           XMLStreamException;

    void resolve(Object model, ParseContext context) throws ResolveException;
}
