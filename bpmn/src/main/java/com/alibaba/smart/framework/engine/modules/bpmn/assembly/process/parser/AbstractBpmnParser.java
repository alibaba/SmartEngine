package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Created by ettear on 16-4-29.
 */
public abstract class AbstractBpmnParser<M extends BaseElement> extends AbstractStAXArtifactParser<M> {

    public AbstractBpmnParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    protected void parseChildren(M model, XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {
        while (this.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof BaseElement) {
                this.parseChild(model, (BaseElement) element);
            }
        }
    }

    protected abstract void parseChild(M model, BaseElement child);
}
