package com.alibaba.smart.framework.engine.bpmn.assembly.event.parser;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = EndEvent.class)

public class EndEventParser extends AbstractBpmnParser<EndEvent> {

    @Override
    public Class<EndEvent> getModelType() {
        return EndEvent.class;
    }

    @Override
    public EndEvent parseModel(XMLStreamReader reader, ParseContext context) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(XmlParseUtil.getString(reader, "id"));

        Map<String, String> properties = super.parseExtendedProperties(reader,  context);
        endEvent.setProperties(properties);

        return endEvent;
    }

}
