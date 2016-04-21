package com.alibaba.smart.framework.process.bpmn.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parse.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parse.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parse.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.model.bpmn.assembly.ProcessDefinition;

public class ProcessDefinitionParser extends AbstractStAXArtifactParser<ProcessDefinition> implements StAXArtifactParser<ProcessDefinition> {

    public ProcessDefinitionParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return ProcessDefinition.type;
    }

    @Override
    public Class<ProcessDefinition> getModelType() {
        return ProcessDefinition.class;
    }

    @Override
    public ProcessDefinition parse(XMLStreamReader reader, ParseContext context) throws ParseException,
                                                                                XMLStreamException {
        
        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setId(this.getString(reader, "id"));
        processDefinition.setVersion(this.getString(reader, "version"));
        processDefinition.setName(this.getString(reader, "name"));

        while (this.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof com.alibaba.smart.framework.process.model.bpmn.assembly.Process) {
                processDefinition.setProcess((com.alibaba.smart.framework.process.model.bpmn.assembly.Process)element);
            }
        }
        return processDefinition; 
    }

}
