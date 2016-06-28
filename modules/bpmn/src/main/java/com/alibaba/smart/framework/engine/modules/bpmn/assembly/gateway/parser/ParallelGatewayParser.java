package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ParallelGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;

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
