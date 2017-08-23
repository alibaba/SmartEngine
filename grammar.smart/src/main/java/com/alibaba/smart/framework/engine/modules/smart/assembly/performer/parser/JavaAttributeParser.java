package com.alibaba.smart.framework.engine.modules.smart.assembly.performer.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.smart.assembly.performer.Java;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXAttributeParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class JavaAttributeParser extends AbstractStAXArtifactParser<Java> implements
    StAXAttributeParser<Java> {

    public JavaAttributeParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
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
