package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.ProcessDefinitionSourceImpl;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER, bindingTo = ProcessDefinitionSourceImpl.class)

public class ProcessDefinitionSourceParser extends AbstractElementParser<ProcessDefinitionSourceImpl>  {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessDefinitionSourceParser.class);

    @Override
    public QName getQname() {
        return ProcessDefinitionSourceImpl.type;
    }

    @Override
    public Class<ProcessDefinitionSourceImpl> getModelType() {
        return ProcessDefinitionSourceImpl.class;
    }

    @Override
    protected ProcessDefinitionSourceImpl parseModel(XMLStreamReader reader, ParseContext context) {
        ProcessDefinitionSourceImpl processDefinition = new ProcessDefinitionSourceImpl();

        return processDefinition;
    }

    @Override
    protected void decorateChild(ProcessDefinitionSourceImpl processDefinitionSource, BaseElement child) {
        if (child instanceof ProcessDefinition) {
            processDefinitionSource.getProcessDefinitionList().add((ProcessDefinition) child);
        }else{
            LOGGER.warn("NOT SUPPORTED:"+child);
        }
    }
}
