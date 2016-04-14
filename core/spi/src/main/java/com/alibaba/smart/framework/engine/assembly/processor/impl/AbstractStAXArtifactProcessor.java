package com.alibaba.smart.framework.engine.assembly.processor.impl;

import com.alibaba.smart.framework.engine.assembly.processor.ProcessorContext;
import com.alibaba.smart.framework.engine.assembly.processor.exception.ProcessorReadException;
import com.alibaba.smart.framework.engine.assembly.processor.exception.ProcessorResolveException;
import com.alibaba.smart.framework.engine.core.LifeCycleListener;
import com.alibaba.smart.framework.engine.extensibility.AssemblyProcessorExtensionPoint;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

/**
 * Abstract StAXArtifactProcessor
 * Created by ettear on 16-4-14.
 */
public abstract class AbstractStAXArtifactProcessor implements LifeCycleListener {

    /**
     * 扩展点注册器
     */
    private ExtensionPointRegistry          extensionPointRegistry;
    private AssemblyProcessorExtensionPoint assemblyProcessorExtensionPoint;

    public AbstractStAXArtifactProcessor(ExtensionPointRegistry extensionPointRegistry) {
        this.extensionPointRegistry = extensionPointRegistry;
    }

    @Override
    public void start() {
        this.assemblyProcessorExtensionPoint = this.extensionPointRegistry.getExtensionPoint(
                AssemblyProcessorExtensionPoint.class);
    }

    @Override
    public void stop() {

    }

    protected String getString(XMLStreamReader reader, String name) {
        return reader.getAttributeValue((String) null, name);
    }

    protected boolean getBoolean(XMLStreamReader reader, String name) {
        String value = reader.getAttributeValue((String)null, name);
        Boolean attr = value== null?null:Boolean.valueOf(value);
        if (attr == null) {
            return false;
        } else {
            return attr.booleanValue();
        }
    }

    public static QName getValueAsQName(XMLStreamReader reader, String value) {
        if(value != null) {
            int index = value.indexOf(58);
            String prefix = index == -1?"":value.substring(0, index);
            String localName = index == -1?value:value.substring(index + 1);
            String ns = reader.getNamespaceContext().getNamespaceURI(prefix);
            if(ns == null) {
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
     * Advance the stream to the next END_ELEMENT event skipping any nested
     * content.
     * @param reader the reader to advance
     * @throws XMLStreamException if there was a problem reading the stream
     */
    protected void skipToEndElement(XMLStreamReader reader) throws XMLStreamException {
        int depth = 0;

        while(reader.hasNext()) {
            int event = reader.next();
            if(event == 1) {
                ++depth;
            } else if(event == 2) {
                if(depth == 0) {
                    return;
                }
                --depth;
            }
        }
    }

    protected Object readElement(XMLStreamReader reader, ProcessorContext context)
            throws ProcessorReadException, XMLStreamException {
        return this.getAssemblyProcessorExtensionPoint().read(reader,context);
    }
    protected void resolveElement(Object model, ProcessorContext context)
            throws ProcessorResolveException {
        this.getAssemblyProcessorExtensionPoint().resolve(model,context);
    }

    // GETTER & SETTER

    protected ExtensionPointRegistry getExtensionPointRegistry() {
        return extensionPointRegistry;
    }

    protected AssemblyProcessorExtensionPoint getAssemblyProcessorExtensionPoint() {
        return assemblyProcessorExtensionPoint;
    }
}
