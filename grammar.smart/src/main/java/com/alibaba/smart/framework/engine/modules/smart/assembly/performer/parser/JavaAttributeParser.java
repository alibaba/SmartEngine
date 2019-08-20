package com.alibaba.smart.framework.engine.modules.smart.assembly.performer.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.smart.assembly.performer.Java;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AttributeParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class JavaAttributeParser extends AbstractElementParser<Java> implements
    AttributeParser<Java> {

    public JavaAttributeParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected Java parseModel(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        return null;
    }

    @Override
    public Java parse(QName attributeName, XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        Java java = new Java();
        String className = reader.getAttributeValue(attributeName.getNamespaceURI(),
            attributeName.getLocalPart());
        java.setClassName(className);
        return java;
    }

    @Override
    public QName getArtifactType() {
        return Java.classQName;
    }

    @Override
    public Class<Java> getModelType() {
        return Java.class;
    }
}
