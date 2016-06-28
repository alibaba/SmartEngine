package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.Base;
import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parser.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;

/**
 * Created by ettear on 16-4-29.
 */
public abstract class AbstractBpmnParser<M extends Base> extends AbstractStAXArtifactParser<M> {

    public AbstractBpmnParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    protected void parseChildren(M model, XMLStreamReader reader, ParseContext context) throws ParseException,
                                                                                       XMLStreamException {
        while (this.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof Base) {
                this.parseChild(model, (Base) element);
            }
        }
    }

    protected abstract void parseChild(M model, Base child);
}
