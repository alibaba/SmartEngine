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
import com.alibaba.smart.framework.process.model.bpmn.assembly.gateway.ExclusiveGateway;


public class ExclusiveGatewayParser  extends AbstractStAXArtifactProcessor implements StAXArtifactProcessor<ExclusiveGateway> {

    public ExclusiveGatewayParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public void resolve(ExclusiveGateway model, ProcessorContext context) throws ProcessorResolveException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public QName getArtifactType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Class<ExclusiveGateway> getModelType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ExclusiveGateway read(XMLStreamReader reader, ProcessorContext context) throws ProcessorReadException,
                                                                                  XMLStreamException {
        // TODO Auto-generated method stub
        return null;
    }

 
}

