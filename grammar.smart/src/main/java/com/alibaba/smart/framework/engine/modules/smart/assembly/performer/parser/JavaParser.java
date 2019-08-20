package com.alibaba.smart.framework.engine.modules.smart.assembly.performer.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.smart.assembly.performer.Java;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.ElementParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXXmlParser;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class JavaParser extends AbstractStAXXmlParser<Java> implements
    ElementParser<Java> {

    public JavaParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public Java parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        Java java = new Java();
        String className=this.getString(reader, "class");
        if(null==className || "".equals(className)){
            throw new ParseException();
        }
        java.setClassName(className);
        java.setAction(this.getString(reader, "action"));
        this.skipToEndElement(reader);
        return java;
    }

    @Override
    public QName getArtifactType() {
        return Java.type;
    }

    @Override
    public Class<Java> getModelType() {
        return Java.class;
    }
}
