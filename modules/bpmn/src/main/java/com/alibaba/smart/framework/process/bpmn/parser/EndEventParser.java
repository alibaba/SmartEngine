package com.alibaba.smart.framework.process.bpmn.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parse.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parse.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ResolveException;
import com.alibaba.smart.framework.engine.assembly.parse.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.model.bpmn.assembly.event.EndEvent;

public class EndEventParser extends AbstractStAXArtifactParser<EndEvent> implements StAXArtifactParser<EndEvent> {

    public EndEventParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public void resolve(EndEvent model, ParseContext context) throws ResolveException {
        model.setUnresolved(false);
    }

    @Override
    public QName getArtifactType() {
        return EndEvent.type;
    }

    @Override
    public Class<EndEvent> getModelType() {
        return EndEvent.class;
    }

    @Override
    public EndEvent parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(this.getString(reader, "id"));

        this.skipToEndElement(reader);
        return endEvent;
    }

}
