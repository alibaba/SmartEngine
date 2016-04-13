package com.alibaba.smart.framework.engine.processor;

import com.alibaba.smart.framework.engine.processor.exception.ProcessorReadException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * An artifact processor that can read attributes from a StAX XMLStreamReader.
 * Created by ettear on 16-4-12.
 */
public interface StAXAttributeProcessor<M> extends ArtifactProcessor<M> {

    /**
     * Reads a model from an XMLStreamReader.
     *
     * @param reader  The XMLStreamReader
     * @param context The context
     * @return A model representation of the input.
     */
    M read(QName attributeName, XMLStreamReader reader, ProcessorContext context) throws ProcessorReadException,
                                                                                         XMLStreamException;
}
