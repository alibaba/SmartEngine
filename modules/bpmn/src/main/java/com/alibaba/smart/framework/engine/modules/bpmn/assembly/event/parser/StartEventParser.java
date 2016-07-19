package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.StartEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension.ExtensionElements;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;

public class StartEventParser extends AbstractBpmnActivityParser<StartEvent> implements StAXArtifactParser<StartEvent> {

    public StartEventParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return StartEvent.type;
    }

    @Override
    public Class<StartEvent> getModelType() {
        return StartEvent.class;
    }

    @Override
    public StartEvent parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(this.getString(reader, "id"));


        while (this.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof ExtensionElements) {
                ExtensionElements element2 = (ExtensionElements) element;
                startEvent.setExtensions(element2);

            }
        }

        return startEvent;

    }

}
