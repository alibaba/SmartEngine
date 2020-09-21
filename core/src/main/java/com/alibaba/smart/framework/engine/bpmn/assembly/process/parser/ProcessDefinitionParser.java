package com.alibaba.smart.framework.engine.bpmn.assembly.process.parser;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.process.ProcessDefinitionImpl;
import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ProcessDefinitionImpl.class)

public class ProcessDefinitionParser extends AbstractElementParser<ProcessDefinition> implements
    ProcessEngineConfigurationAware {



    @Override
    public QName getQname() {
        return ProcessDefinitionImpl.qtype;
    }

    @Override
    public Class<ProcessDefinition> getModelType() {
        return ProcessDefinition.class;
    }

    @Override
    public ProcessDefinition parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {

        ProcessDefinition processDefinition = new ProcessDefinitionImpl();
        processDefinition.setId(XmlParseUtil.getString(reader, "id"));

        String version = XmlParseUtil.getString(reader, "version");

        String versionTag = XmlParseUtil.getString(reader, "versionTag");

        //优先使用versionTag；versionTag是 camunda等设计器的特性，此处是为了兼容。
        if(StringUtil.isNotEmpty(versionTag)){
            processDefinition.setVersion(versionTag);
        }else {
            processDefinition.setVersion(version);
        }

        processDefinition.setName(XmlParseUtil.getString(reader, "name"));


        List<BaseElement> elements = CollectionUtil.newArrayList();

        Map<String, IdBasedElement> idBasedElementMap = MapUtil.newLinkedHashMap();

        while (XmlParseUtil.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof BaseElement) {

                if(element instanceof ExtensionElements){
                    processDefinition.setExtensionElements((ExtensionElements)element);
                }


                elements.add((BaseElement) element);
                if(element instanceof IdBasedElement){
                    IdBasedElement idBasedElement = (IdBasedElement)element;

                    if(null != idBasedElementMap.get(idBasedElement.getId())){
                        throw new EngineException("duplicated id found:"+idBasedElement.getId());
                    }else{
                        idBasedElementMap.put(idBasedElement.getId(), idBasedElement);

                    }

                }
            }
        }

        processDefinition.setBaseElementList(elements);
        processDefinition.setIdBasedElementMap(idBasedElementMap);

        return processDefinition;
    }

}
