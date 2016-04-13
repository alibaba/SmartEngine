package com.alibaba.smart.framework.engine.extensibility;

import com.alibaba.smart.framework.engine.processor.ProcessorContext;
import com.alibaba.smart.framework.engine.processor.exception.ProcessorReadException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * XML处理器扩展点
 * Created by ettear on 16-4-12.
 */
public interface ProcessorExtensionPoint extends ClassLoaderExtensionPoint {

    /**
     * Reads a model from an XMLStreamReader.
     *
     * @param reader  The XMLStreamReader
     * @param context The context
     * @return A model representation of the input.
     */
    Object read(XMLStreamReader reader, ProcessorContext context) throws ProcessorReadException, XMLStreamException;

    /**
     * Reads a model from an XMLStreamReader.
     *
     * @param reader  The XMLStreamReader
     * @param context The context
     * @return A model representation of the input.
     */
    Object readAttribute(QName attributeName, XMLStreamReader reader, ProcessorContext context)
            throws ProcessorReadException,
                   XMLStreamException;
}
