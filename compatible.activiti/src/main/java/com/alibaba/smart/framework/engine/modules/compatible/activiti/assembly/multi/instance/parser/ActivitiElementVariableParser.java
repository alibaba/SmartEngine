package com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.ActivitiElementVariable;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.AttributeParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ActivitiElementVariable.class)

public class ActivitiElementVariableParser extends AbstractElementParser<ActivitiElementVariable> implements
    AttributeParser<ActivitiElementVariable> {

    //public ActivitiElementVariableParser(ExtensionPointRegistry extensionPointRegistry) {
    //    super(extensionPointRegistry);
    //}

    @Override
    public QName getQname() {
        return ActivitiElementVariable.type;
    }

    @Override
    public Class<ActivitiElementVariable> getModelType() {
        return ActivitiElementVariable.class;
    }

    @Override
    public ActivitiElementVariable parseAttribute(QName attributeName, XMLStreamReader reader, ParseContext context) {
        ActivitiElementVariable activitiElementVariable = new ActivitiElementVariable();
        activitiElementVariable.setName(reader.getAttributeValue(attributeName.getNamespaceURI(),
            attributeName.getLocalPart()));
        return activitiElementVariable;
    }
}
