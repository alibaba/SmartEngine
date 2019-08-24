package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.Process;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.ProcessDefinition;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

public class ProcessDefinitionParser extends AbstractElementParser<ProcessDefinition>  {

    public ProcessDefinitionParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getQname() {
        return ProcessDefinition.type;
    }

    @Override
    public Class<ProcessDefinition> getModelType() {
        return ProcessDefinition.class;
    }

    @Override
    protected ProcessDefinition parseModel(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        ProcessDefinition processDefinition = new ProcessDefinition();
        processDefinition.setId(XmlParseUtil.getString(reader, "id"));
        processDefinition.setVersion(XmlParseUtil.getString(reader, "version"));
        processDefinition.setName(XmlParseUtil.getString(reader, "name"));
        return processDefinition;
    }

    @Override
    protected void parseSingleChild(ProcessDefinition model, BaseElement child) throws ParseException {
        if (child instanceof Process) {
            model.setProcess((Process) child);
        }
    }
}
