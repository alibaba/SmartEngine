package com.alibaba.smart.framework.process.bpmn.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parse.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parse.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parse.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.model.bpmn.assembly.gateway.ParallelGateway;

public class ParallelGatewayParser extends AbstractStAXArtifactParser<ParallelGateway> implements StAXArtifactParser<ParallelGateway> {

    public ParallelGatewayParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<ParallelGateway> getModelType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ParallelGateway parse(XMLStreamReader reader, ParseContext context) throws ParseException,
                                                                              XMLStreamException {
        // TODO Auto-generated method stub
        return null;
    }

}
