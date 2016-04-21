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

public class StartEventParser extends AbstractStAXArtifactProcessor implements StAXArtifactProcessor<StartEvent> {

    public StartEventParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public void resolve(StartEvent model, ProcessorContext context) throws ProcessorResolveException {
        //TODO 何时设置true?
        
        model.setUnresolved(false);

    }

    @Override
    public QName getArtifactType() {
        return StartEvent.type;
    }

    @Override
    public Class<StartEvent> getModelType() {
        //TODO 可否通过获取泛型,避免覆写这个方法
        return StartEvent.class;
    }

    @Override
    public StartEvent read(XMLStreamReader reader, ProcessorContext context) throws ProcessorReadException,
                                                                            XMLStreamException {
        
        StartEvent startEvent = new StartEvent();
        startEvent.setId(this.getString(reader, "id"));

        this.skipToEndElement(reader);
        return startEvent;
        
    }

}
