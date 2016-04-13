package com.alibaba.smart.framework.engine.processor;

import com.alibaba.smart.framework.engine.processor.exception.ProcessorReadException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Created by ettear on 16-4-12.
 */
public interface StAXArtifactProcessor<M> extends ArtifactProcessor<M> {

    /**
     * Reads a model from an XMLStreamReader.
     *
     * @param reader  The XMLStreamReader
     * @param context The context
     * @return A model representation of the input.
     */
    M read(XMLStreamReader reader, ProcessorContext context) throws ProcessorReadException, XMLStreamException;
}
