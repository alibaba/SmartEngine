package com.alibaba.smart.framework.engine.bpmn.assembly.process.parser;

import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.process.ProcessDefinitionSourceImpl;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ProcessDefinitionSourceImpl.class)

public class ProcessDefinitionSourceParser extends AbstractElementParser<ProcessDefinitionSourceImpl>
  implements   ProcessEngineConfigurationAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessDefinitionSourceParser.class);

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
    protected void decorateChild(ProcessDefinitionSourceImpl processDefinitionSource, BaseElement child, ParseContext context) {
        if (child instanceof ProcessDefinition) {
            processDefinitionSource.getProcessDefinitionList().add((ProcessDefinition) child);
        }else{
            LOGGER.warn("NOT SUPPORTED:"+child);
        }
    }
}
