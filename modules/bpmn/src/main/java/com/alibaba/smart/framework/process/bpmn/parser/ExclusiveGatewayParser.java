package com.alibaba.smart.framework.process.bpmn.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parse.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parse.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parse.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.model.bpmn.assembly.gateway.ExclusiveGateway;

public class ExclusiveGatewayParser extends AbstractStAXArtifactParser<ExclusiveGateway> implements StAXArtifactParser<ExclusiveGateway> {

    public ExclusiveGatewayParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }



    @Override
    public QName getArtifactType() {
        return ExclusiveGateway.type;
    }

    @Override
    public Class<ExclusiveGateway> getModelType() {
        return ExclusiveGateway.class;
    }

    @Override
    public ExclusiveGateway parse(XMLStreamReader reader, ParseContext context) throws ParseException,
                                                                               XMLStreamException {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(this.getString(reader, "id"));
        exclusiveGateway.setName(this.getString(reader, "name"));


        this.skipToEndElement(reader);
        return exclusiveGateway;
    }

}
