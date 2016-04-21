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
import com.alibaba.smart.framework.process.model.bpmn.assembly.event.StartEvent;

public class EndEventParser extends AbstractStAXArtifactProcessor implements StAXArtifactProcessor<StartEvent> {

    public EndEventParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public void resolve(StartEvent model, ProcessorContext context) throws ProcessorResolveException {
        // TODO Auto-generated method stub

    }

    @Override
    public QName getArtifactType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<StartEvent> getModelType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StartEvent read(XMLStreamReader reader, ProcessorContext context) throws ProcessorReadException,
                                                                            XMLStreamException {
        // TODO Auto-generated method stub
        return null;
    }

}
