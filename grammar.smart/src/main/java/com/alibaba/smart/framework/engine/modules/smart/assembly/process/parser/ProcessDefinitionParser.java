package com.alibaba.smart.framework.engine.modules.smart.assembly.process.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.Process;
import com.alibaba.smart.framework.engine.modules.smart.assembly.process.ProcessDefinition;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class ProcessDefinitionParser extends AbstractElementParser<ProcessDefinition> {

    public ProcessDefinitionParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected ProcessDefinition parseModel(XMLStreamReader reader, ParseContext context) {
        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setId(this.getString(reader, "id"));
        processDefinition.setVersion(this.getString(reader, "version"));
        processDefinition.setName(this.getString(reader, "name"));
        return processDefinition;
    }

    @Override
    protected void parseChild(ProcessDefinition processDefinition, BaseElement child) {
        if (child instanceof Process) {
            processDefinition.setProcess((Process)child);
        }
    }

    @Override
    public QName getArtifactType() {
        return ProcessDefinition.type;
    }

    @Override
    public Class<ProcessDefinition> getModelType() {
        return ProcessDefinition.class;
    }
}
