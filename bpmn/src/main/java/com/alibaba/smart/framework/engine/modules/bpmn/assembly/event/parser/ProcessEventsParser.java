package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.ProcessEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.ProcessEvents;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.ElementParser;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

/**
 * Created by dongdongzdd on 16/9/20.
 */
public class ProcessEventsParser extends AbstractElementParser<ProcessEvents> implements ElementParser<ProcessEvents> {

    public ProcessEventsParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return ProcessEvents.NameType;
    }

    @Override
    public Class<ProcessEvents> getModelType() {
        return ProcessEvents.class;
    }

    @Override
    public ProcessEvents parseModel(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {

        ProcessEvents events = new ProcessEvents();
        if (null == events.getEvents()) {
            List<ProcessEvent> list = new ArrayList();
            events.setEvents(list);
        }
        return events;
    }


    @Override
    protected void parseChild(ProcessEvents model, BaseElement child) {

        if (child instanceof ProcessEvent) {
            model.getEvents().add((ProcessEvent) child);
        }

    }


}
