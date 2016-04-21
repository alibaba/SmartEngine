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
        // TODO Auto-generated constructor stub
    }

    @Override
    public void resolve(EndEvent model, ProcessorContext context) throws ProcessorResolveException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public QName getArtifactType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<EndEvent> getModelType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EndEvent read(XMLStreamReader reader, ProcessorContext context) throws ProcessorReadException,
                                                                          XMLStreamException {
        // TODO Auto-generated method stub
        return null;
    }

    

}
