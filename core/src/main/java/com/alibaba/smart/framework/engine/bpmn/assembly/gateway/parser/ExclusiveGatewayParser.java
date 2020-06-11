package com.alibaba.smart.framework.engine.bpmn.assembly.gateway.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.ExclusiveGateway;
import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ExclusiveGateway.class)

public class ExclusiveGatewayParser extends AbstractBpmnParser<ExclusiveGateway> {

    @Override
    public QName getQname() {
        return ExclusiveGateway.type;
    }

    @Override
    public Class<ExclusiveGateway> getModelType() {
        return ExclusiveGateway.class;
    }

    @Override
    public ExclusiveGateway parseModel(XMLStreamReader reader, ParseContext context) {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(XmlParseUtil.getString(reader, "id"));
        exclusiveGateway.setName(XmlParseUtil.getString(reader, "name"));


        Map<String, String> properties = super.parseExtendedProperties(reader,  context);
        exclusiveGateway.setProperties(properties);

        return exclusiveGateway;
    }

}
