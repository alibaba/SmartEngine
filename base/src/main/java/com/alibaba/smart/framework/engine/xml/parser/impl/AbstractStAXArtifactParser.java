package com.alibaba.smart.framework.engine.xml.parser.impl;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.listener.LifeCycleListener;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.xml.parser.ArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.AssemblyParserExtensionPoint;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.exception.ResolveException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Abstract StAXArtifactParser Created by ettear on 16-4-14.
 */
public abstract class AbstractStAXArtifactParser<M extends BaseElement> implements LifeCycleListener, ArtifactParser<M> {

//    private static final Logger          LOGGER = LoggerFactory.getLogger(AbstractStAXArtifactParser.class);


    /**
     * 扩展点注册器
     */
    private ExtensionPointRegistry extensionPointRegistry;
    private AssemblyParserExtensionPoint assemblyParserExtensionPoint;

    public AbstractStAXArtifactParser(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.assemblyParserExtensionPoint = this.extensionPointRegistry.getExtensionPoint(AssemblyParserExtensionPoint.class);
    }

    @Override
    public void stop() {

    }

    @Override
    public void resolve(M model, ParseContext context) throws ResolveException {
        model.setUnresolved(false);
    }

    protected String getString(XMLStreamReader reader, String name) {
        return reader.getAttributeValue((String) null, name);
    }

    protected boolean getBoolean(XMLStreamReader reader, String name) {
        String value = reader.getAttributeValue((String) null, name);
        Boolean attr = value == null ? null : Boolean.valueOf(value);
        if (attr == null) {
            return false;
        } else {
            return attr.booleanValue();
        }
    }

    public static QName getValueAsQName(XMLStreamReader reader, String value) {
        if (value != null) {
            int index = value.indexOf(58);
            String prefix = index == -1 ? "" : value.substring(0, index);
            String localName = index == -1 ? value : value.substring(index + 1);
            String ns = reader.getNamespaceContext().getNamespaceURI(prefix);
            if (ns == null) {
                ns = "";
            }

            return new QName(ns, localName, prefix);
        } else {
            return null;
        }
    }

    protected boolean nextChildElement(XMLStreamReader reader) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();


            // LOGGER.debug(event + reader.getEventType() + "");

            if (event == END_ELEMENT) {
                return false;
            }
            if (event == START_ELEMENT) {
                return true;
            }
        }
        return false;
    }

    /**
     * Advance the stream to the next END_ELEMENT event skipping any nested content.
     *
     * @param reader the reader to advance
     * @throws XMLStreamException if there was a problem reading the stream
     */
    protected void skipToEndElement(XMLStreamReader reader) throws XMLStreamException {
        int depth = 0;

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == START_ELEMENT) {
                ++depth;
            } else if (event == END_ELEMENT) {
                if (depth == 0) {
                    return;
                }
                --depth;
            }
        }
    }

    protected Object readElement(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {
        return this.getAssemblyParserExtensionPoint().parse(reader, context);
    }

    protected void resolveElement(Object model, ParseContext context) throws ResolveException {
        this.getAssemblyParserExtensionPoint().resolve(model, context);
    }

    protected Object readAttribute(QName attributeName,XMLStreamReader reader, ParseContext context) throws ParseException,
        XMLStreamException {
        return this.getAssemblyParserExtensionPoint().readAttribute(attributeName,reader, context);
    }

    // GETTER & SETTER

    protected ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }

    protected AssemblyParserExtensionPoint getAssemblyParserExtensionPoint() {
        return assemblyParserExtensionPoint;
    }
}
