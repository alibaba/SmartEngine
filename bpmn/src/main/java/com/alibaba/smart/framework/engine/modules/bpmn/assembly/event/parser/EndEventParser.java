package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.EndEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnActivityParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class EndEventParser extends AbstractBpmnActivityParser<EndEvent>   {

    public EndEventParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

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
        return endEvent;
    }

}
