package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.StartEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.xml.parser.ElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

public class StartEventParser extends AbstractBpmnActivityParser<StartEvent> implements ElementParser<StartEvent> {

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
    public StartEvent parseModel(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(this.getString(reader, "id"));
        startEvent.setStartActivity(true);
        return startEvent;
    }

}
