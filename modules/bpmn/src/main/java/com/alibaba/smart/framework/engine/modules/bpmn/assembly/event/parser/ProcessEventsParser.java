package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.ProcessEvent;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.ProcessEvents;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.google.common.collect.Lists;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.List;

/**
 * Created by dongdongzdd on 16/9/20.
 */
public class ProcessEventsParser extends AbstractBpmnParser<ProcessEvents> implements StAXArtifactParser<ProcessEvents> {

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
    public ProcessEvents parse(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {

        ProcessEvents events = new ProcessEvents();
        if (null == events.getEvents()) {
            List<ProcessEvent> list = Lists.newArrayList();
            events.setEvents(list);
        }
        this.parseChildren(events, reader, context);
        return events;

    }



    @Override
    protected void parseChild(ProcessEvents model, BaseElement child) {

        if (child instanceof ProcessEvent) {
            model.getEvents().add((ProcessEvent) child);
        }

    }


}
