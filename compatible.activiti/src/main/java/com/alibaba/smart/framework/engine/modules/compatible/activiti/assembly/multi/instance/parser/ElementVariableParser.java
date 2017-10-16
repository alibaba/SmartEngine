package com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance.ElementVariable;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXAttributeParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class ElementVariableParser extends AbstractStAXArtifactParser<ElementVariable> implements
    StAXAttributeParser<ElementVariable> {

    public ElementVariableParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return ElementVariable.type;
    }

    @Override
    public Class<ElementVariable> getModelType() {
        return ElementVariable.class;
    }

    @Override
    public ElementVariable parse(QName attributeName, XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        ElementVariable elementVariable = new ElementVariable();
        elementVariable.setName(reader.getAttributeValue(attributeName.getNamespaceURI(),
            attributeName.getLocalPart()));
        return elementVariable;
    }
}