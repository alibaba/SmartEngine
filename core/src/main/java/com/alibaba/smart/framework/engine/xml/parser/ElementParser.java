package com.alibaba.smart.framework.engine.xml.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/** Created by ettear on 16-4-12. */
public interface ElementParser<M> extends BaseXmlParser {

    /**
     * Reads a model from an XMLStreamReader.
     *
     * @param reader The XMLStreamReader
     * @param context The context
     * @return A model representation of the input.
     */
    M parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException;
}
