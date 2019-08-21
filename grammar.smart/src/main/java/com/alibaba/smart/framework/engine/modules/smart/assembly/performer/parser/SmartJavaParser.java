package com.alibaba.smart.framework.engine.modules.smart.assembly.performer.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.smart.assembly.performer.Java;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class SmartJavaParser extends AbstractElementParser<Java>   {

    public SmartJavaParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public Java parseElement(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        Java java = new Java();
        String className= XmlParseUtil.getString(reader, "class");
        if(null==className || "".equals(className)){
            throw new ParseException();
        }
        java.setClassName(className);
        java.setAction(XmlParseUtil.getString(reader, "action"));
        XmlParseUtil.skipToEndElement(reader);
        return java;
    }

    @Override
    protected Java parseModel(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        return null;
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
