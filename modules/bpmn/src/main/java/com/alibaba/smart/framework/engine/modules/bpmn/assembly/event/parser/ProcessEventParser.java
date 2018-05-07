package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
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
    public ProcessEvent parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {

        ProcessEvent event = new ProcessEvent();
        event.setType(this.getStringThrowException(reader,"type"));
        event.setId(this.getStringThrowException(reader,"id"));
        event.setMethod(this.getStringThrowException(reader,"method"));
        String signal =  this.getString(reader,"signal");
        //todo 目前只支持abort 信号 后续再加
        if (null != signal && !signal.equals("abort") && !signal.equals("end")) {
            throw new ParseException("id : "+event.getId()+" can not have this signal: "+signal+" !");
        }
        event.setSignal(signal);
        event.setBean(this.getStringThrowException(reader,"bean"));
        while (this.nextChildElement(reader)) {

        }

        return event;

    }

}
