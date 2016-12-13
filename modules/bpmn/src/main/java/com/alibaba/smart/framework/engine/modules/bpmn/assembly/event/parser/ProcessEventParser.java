package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.parser;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.action.Action;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.ProcessEvent;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * Created by dongdongzdd on 16/9/20.
 */
public class ProcessEventParser extends AbstractStAXArtifactParser<ProcessEvent> implements StAXArtifactParser<ProcessEvent> {

    public ProcessEventParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return ProcessEvent.NameType;
    }

    @Override
    public Class<ProcessEvent> getModelType() {
        return ProcessEvent.class;
    }

    @Override
    public ProcessEvent parse(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {

        ProcessEvent event = new ProcessEvent();
        event.setType(this.getString(reader,"type"));
        event.setId(this.getString(reader,"id"));
        event.setMethod(this.getString(reader,"method"));
        String signal =  this.getString(reader,"signal");
        if (!signal.equals("abort")) {
            throw new EngineException("id : "+event.getId()+" can not have this signal: "+signal+" !");
        }
        while (this.nextChildElement(reader)) {

        }

        return event;

    }

}
