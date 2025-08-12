package com.alibaba.smart.framework.engine.bpmn.assembly.gateway.parser;

import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.InclusiveGateway;
import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.InclusiveGateway;
import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

import javax.xml.stream.XMLStreamReader;
import java.util.Map;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = InclusiveGateway.class)

public class InclusiveGatewayParser extends AbstractBpmnParser<InclusiveGateway> {

    @Override
    public Class<InclusiveGateway> getModelType() {
        return InclusiveGateway.class;
    }

    @Override
    public InclusiveGateway parseModel(XMLStreamReader reader, ParseContext context) {
        InclusiveGateway inclusiveGateway = new InclusiveGateway();
        inclusiveGateway.setId(XmlParseUtil.getString(reader, "id"));
        inclusiveGateway.setName(XmlParseUtil.getString(reader, "name"));


        Map<String, String> properties = super.parseExtendedProperties(reader,  context);
        inclusiveGateway.setProperties(properties);

        return inclusiveGateway;
    }

}
