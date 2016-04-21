package com.alibaba.smart.framework.engine.assembly.parse;

import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

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
