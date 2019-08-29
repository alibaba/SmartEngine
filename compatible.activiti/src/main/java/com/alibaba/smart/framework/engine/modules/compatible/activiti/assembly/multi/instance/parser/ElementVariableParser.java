package com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.CompletionCondition;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.ElementVariable;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AttributeParser;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER,binding = ElementVariable.class)

public class ElementVariableParser extends AbstractElementParser<ElementVariable> implements
    AttributeParser<ElementVariable> {

    //public ElementVariableParser(ExtensionPointRegistry extensionPointRegistry) {
    //    super(extensionPointRegistry);
    //}

    @Override
    public QName getQname() {
        return ElementVariable.type;
    }

    @Override
    public Class<ElementVariable> getModelType() {
        return ElementVariable.class;
    }

    @Override
    public ElementVariable parseAttribute(QName attributeName, XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        ElementVariable elementVariable = new ElementVariable();
        elementVariable.setName(reader.getAttributeValue(attributeName.getNamespaceURI(),
            attributeName.getLocalPart()));
        return elementVariable;
    }
}
