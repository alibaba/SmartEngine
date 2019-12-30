package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.StartEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = StartEvent.class)

public class StartEventParser extends AbstractBpmnParser<StartEvent>   {



    @Override
    public QName getQname() {
        return StartEvent.type;
    }

    @Override
    public Class<StartEvent> getModelType() {
        return StartEvent.class;
    }

    @Override
    public StartEvent parseModel(XMLStreamReader reader, ParseContext context) {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(XmlParseUtil.getString(reader, "id"));
        startEvent.setStartActivity(true);

        Map<String, String> properties = super.parseExtendedProperties(reader,  context);
        startEvent.setProperties(properties);
        return startEvent;
    }

}
