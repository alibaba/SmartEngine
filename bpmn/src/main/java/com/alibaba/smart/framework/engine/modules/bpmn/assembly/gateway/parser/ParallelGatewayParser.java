package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ParallelGatewayParser extends AbstractBpmnActivityParser<ParallelGateway> implements StAXArtifactParser<ParallelGateway> {

    public ParallelGatewayParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return ParallelGateway.type;
    }

    @Override
    public Class<ParallelGateway> getModelType() {
        return ParallelGateway.class;
    }

    @Override
    public ParallelGateway parse(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {

        ParallelGateway parallelGateway = new ParallelGateway();
        parallelGateway.setId(this.getString(reader, "id"));

        this.parseChildren(parallelGateway, reader, context);
        return parallelGateway;
    }

}