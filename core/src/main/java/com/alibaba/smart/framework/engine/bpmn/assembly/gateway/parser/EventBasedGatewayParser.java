package com.alibaba.smart.framework.engine.bpmn.assembly.gateway.parser;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.gateway.EventBasedGateway;
import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = EventBasedGateway.class)

public class EventBasedGatewayParser extends AbstractBpmnParser<EventBasedGateway> {

    @Override
    public Class<EventBasedGateway> getModelType() {
        return EventBasedGateway.class;
    }

    @Override
    public EventBasedGateway parseModel(XMLStreamReader reader, ParseContext context) {
        EventBasedGateway eventBasedGateway = new EventBasedGateway();
        eventBasedGateway.setId(XmlParseUtil.getString(reader, "id"));
        eventBasedGateway.setName(XmlParseUtil.getString(reader, "name"));

        Map<String, String> properties = super.parseExtendedProperties(reader, context);
        eventBasedGateway.setProperties(properties);

        return eventBasedGateway;
    }

}
