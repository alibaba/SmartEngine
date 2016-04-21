package com.alibaba.smart.framework.process.bpmn.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parse.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parse.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parse.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.model.bpmn.assembly.event.StartEvent;

public class StartEventParser extends AbstractStAXArtifactParser<StartEvent> implements StAXArtifactParser<StartEvent> {

    public StartEventParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return StartEvent.type;
    }

    @Override
    public Class<StartEvent> getModelType() {
        // TODO 可否通过获取泛型,避免覆写这个方法
        return StartEvent.class;
    }

    @Override
    public StartEvent parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {

        StartEvent startEvent = new StartEvent();
        startEvent.setId(this.getString(reader, "id"));

        this.skipToEndElement(reader);
        return startEvent;

    }

}
