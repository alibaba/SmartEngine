package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.Process;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.ProcessDefinition;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

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
            if (element instanceof Process) {
                processDefinition.setProcess((Process) element);
            }
        }
        return processDefinition;
    }

}
