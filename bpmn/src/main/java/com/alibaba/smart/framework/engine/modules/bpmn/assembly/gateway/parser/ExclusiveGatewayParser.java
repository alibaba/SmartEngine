package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXXmlParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

public class ExclusiveGatewayParser extends AbstractBpmnActivityParser<ExclusiveGateway> implements
    StAXXmlParser<ExclusiveGateway> {

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
    public ExclusiveGateway parseModel(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(this.getString(reader, "id"));
        return exclusiveGateway;
    }

}
