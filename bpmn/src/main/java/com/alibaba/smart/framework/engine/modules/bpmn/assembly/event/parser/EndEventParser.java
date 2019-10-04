package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.parser;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER, bindingTo = EndEvent.class)

public class EndEventParser extends AbstractBpmnParser<EndEvent> {



    //@Override
    //public void resolve(EndEvent model, ParseContext context) throws ResolveException {
    //    model.setUnresolved(false);
    //}

    @Override
    public QName getQname() {
        return EndEvent.type;
    }

    @Override
    public Class<EndEvent> getModelType() {
        return EndEvent.class;
    }

    @Override
    public EndEvent parseModel(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(XmlParseUtil.getString(reader, "id"));

        Map<String, String> properties = super.parseExtendedProperties(reader,  context);
        endEvent.setProperties(properties);

        return endEvent;
    }

}
