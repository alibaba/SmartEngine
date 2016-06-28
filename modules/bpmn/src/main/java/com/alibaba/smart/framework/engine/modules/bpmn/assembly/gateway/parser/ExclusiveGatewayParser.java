package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;

public class ExclusiveGatewayParser extends AbstractBpmnActivityParser<ExclusiveGateway> implements StAXArtifactParser<ExclusiveGateway> {

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

        this.parseChildren(exclusiveGateway, reader, context);
        return exclusiveGateway;
    }

}
