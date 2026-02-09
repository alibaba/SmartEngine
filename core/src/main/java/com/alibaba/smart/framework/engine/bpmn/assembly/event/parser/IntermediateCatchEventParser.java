package com.alibaba.smart.framework.engine.bpmn.assembly.event.parser;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.event.IntermediateCatchEvent;
import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = IntermediateCatchEvent.class)

public class IntermediateCatchEventParser extends AbstractBpmnParser<IntermediateCatchEvent> {

    @Override
    public Class<IntermediateCatchEvent> getModelType() {
        return IntermediateCatchEvent.class;
    }

    @Override
    public IntermediateCatchEvent parseModel(XMLStreamReader reader, ParseContext context) {
        IntermediateCatchEvent intermediateCatchEvent = new IntermediateCatchEvent();
        intermediateCatchEvent.setId(XmlParseUtil.getString(reader, "id"));
        intermediateCatchEvent.setName(XmlParseUtil.getString(reader, "name"));

        Map<String, String> properties = super.parseExtendedProperties(reader, context);
        intermediateCatchEvent.setProperties(properties);

        return intermediateCatchEvent;
    }

}
