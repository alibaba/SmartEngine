package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.Process;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.ProcessDefinitionImpl;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER, bindingTo = ProcessDefinitionImpl.class)

public class ProcessDefinitionParser extends AbstractElementParser<ProcessDefinitionImpl>  {


    @Override
    public QName getQname() {
        return ProcessDefinitionImpl.type;
    }

    @Override
    public Class<ProcessDefinitionImpl> getModelType() {
        return ProcessDefinitionImpl.class;
    }

    @Override
    protected ProcessDefinitionImpl parseModel(XMLStreamReader reader, ParseContext context) {
        ProcessDefinitionImpl processDefinition = new ProcessDefinitionImpl();
        processDefinition.setId(XmlParseUtil.getString(reader, "id"));
        processDefinition.setVersion(XmlParseUtil.getString(reader, "version"));
        processDefinition.setName(XmlParseUtil.getString(reader, "name"));
        return processDefinition;
    }

    @Override
    protected void singingMagic(ProcessDefinitionImpl model, BaseElement child) {
        if (child instanceof Process) {
            model.setProcess((Process) child);
        }
    }
}
