package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.StartEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER,binding = StartEvent.class)

public class StartEventParser extends AbstractBpmnActivityParser<StartEvent>   {



    @Override
    public QName getQname() {
        return StartEvent.type;
    }

    @Override
    public Class<StartEvent> getModelType() {
        return StartEvent.class;
    }

    @Override
    public StartEvent parseModel(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(XmlParseUtil.getString(reader, "id"));
        startEvent.setStartActivity(true);
        return startEvent;
    }

}
