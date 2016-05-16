package com.alibaba.smart.framework.engine.assembly.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;

/**
 * Created by ettear on 16-4-12.
 */
public interface StAXArtifactParser<M> extends ArtifactParser<M> {

    /**
     * Reads a model from an XMLStreamReader.
     *
     * @param reader  The XMLStreamReader
     * @param context The context
     * @return A model representation of the input.
     */
    M parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException;
}
