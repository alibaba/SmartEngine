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
import com.alibaba.smart.framework.process.model.bpmn.assembly.gateway.ExclusiveGateway;

public class ExclusiveGatewayParser extends AbstractStAXArtifactParser<ExclusiveGateway> implements StAXArtifactParser<ExclusiveGateway> {

    public ExclusiveGatewayParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public void resolve(ExclusiveGateway model, ParseContext context) throws ResolveException {
        // TODO Auto-generated method stub

    }

    @Override
    public QName getArtifactType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<ExclusiveGateway> getModelType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExclusiveGateway parse(XMLStreamReader reader, ParseContext context) throws ParseException,
                                                                               XMLStreamException {
        // TODO Auto-generated method stub
        return null;
    }

}
