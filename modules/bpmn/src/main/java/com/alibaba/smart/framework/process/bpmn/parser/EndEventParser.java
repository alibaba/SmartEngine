package com.alibaba.smart.framework.process.bpmn.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.processor.ProcessorContext;
import com.alibaba.smart.framework.engine.assembly.processor.StAXArtifactProcessor;
import com.alibaba.smart.framework.engine.assembly.processor.exception.ProcessorReadException;
import com.alibaba.smart.framework.engine.assembly.processor.exception.ProcessorResolveException;
import com.alibaba.smart.framework.engine.assembly.processor.impl.AbstractStAXArtifactProcessor;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.model.bpmn.assembly.event.EndEvent;

public class EndEventParser extends AbstractStAXArtifactProcessor implements StAXArtifactProcessor<EndEvent> {

    public EndEventParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public void resolve(EndEvent model, ProcessorContext context) throws ProcessorResolveException {
        model.setUnresolved(false);
    }

    @Override
    public QName getArtifactType() {
        return EndEvent.type;
    }

    @Override
    public Class<EndEvent> getModelType() {
        return EndEvent.class;
    }

    @Override
    public EndEvent read(XMLStreamReader reader, ProcessorContext context) throws ProcessorReadException,
                                                                          XMLStreamException {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(this.getString(reader, "id"));

        this.skipToEndElement(reader);
        return endEvent;
    }

    

}
