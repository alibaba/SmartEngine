package com.alibaba.smart.framework.engine.modules.smart.assembly.performer.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.smart.assembly.performer.Java;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AttributeParser;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */

@ExtensionBinding(type = ExtensionConstant.ATTRIBUTE_PARSER,binding = Java.class)
    //FIXME
public class SmartJavaAttributeParser extends AbstractElementParser<Java> implements
    AttributeParser<Java> {



    @Override
    protected Java parseModel(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        return null;
    }

    @Override
    public Java parseAttribute(QName attributeName, XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        Java java = new Java();
        String className = reader.getAttributeValue(attributeName.getNamespaceURI(),
            attributeName.getLocalPart());
        java.setClassName(className);
        return java;
    }

    @Override
    public QName getQname() {
        return Java.classQName;
    }

    @Override
    public Class<Java> getModelType() {
        return Java.class;
    }
}
