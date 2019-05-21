package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway.ComplexGateway;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

/**
 * ${DESCRIPTION}
 *
 * @author zilong.jiangzl
 * @create 2019-05-21 上午11:06
 */
public class ComplexGatewayParser extends AbstractBpmnActivityParser<ComplexGateway> implements StAXArtifactParser<ComplexGateway> {

    public ComplexGatewayParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return ComplexGateway.type;
    }

    @Override
    public Class<ComplexGateway> getModelType() {
        return ComplexGateway.class;
    }

    @Override
    public ComplexGateway parseModel(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {
        ComplexGateway complexGateway = new ComplexGateway();
        complexGateway.setId(this.getString(reader, "id"));
        return complexGateway;
    }

}
