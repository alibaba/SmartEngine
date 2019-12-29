package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.common.util.CollectionUtil;
import com.alibaba.smart.framework.engine.common.util.IdAndVersionBuilder;
import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.ProcessDefinitionImpl;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;


@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER, bindingTo = ProcessDefinitionImpl.class)

public class ProcessDefinitionParser extends AbstractElementParser<ProcessDefinition>  {



    @Override
    public QName getQname() {
        return ProcessDefinitionImpl.type;
    }

    @Override
    public Class<ProcessDefinition> getModelType() {
        return ProcessDefinition.class;
    }

    @Override
    public ProcessDefinition parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {

        ProcessDefinition processDefinition = new ProcessDefinitionImpl();
        processDefinition.setId(XmlParseUtil.getString(reader, "id"));
        processDefinition.setVersion(XmlParseUtil.getString(reader, "version"));
        processDefinition.setName(XmlParseUtil.getString(reader, "name"));
        processDefinition.setIdAndVersion(IdAndVersionBuilder.buildProcessDefinitionKey(processDefinition.getId(),processDefinition.getVersion()));


        List<BaseElement> elements = CollectionUtil.newArrayList();

        Map<String, IdBasedElement> idBasedElementMap = MapUtil.newLinkedHashMap();

        while (XmlParseUtil.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            if (element instanceof BaseElement) {
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
