package com.alibaba.smart.framework.engine.modules.smart.assembly.performer.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.modules.smart.assembly.performer.Java;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */

@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER, bindingTo = Java.class)

public class SmartJavaParser extends AbstractElementParser<Java>   {



    @Override
    public Java parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        Java java = new Java();
        String className= XmlParseUtil.getString(reader, "class");

        //fixme
        if(null==className || "".equals(className)){
            throw new ParseException("");
        }
        java.setClassName(className);

        ////fixme
        //java.setAction(XmlParseUtil.getString(reader, "action"));
        XmlParseUtil.skipToEndElement(reader);
        return java;
    }

    @Override
    protected Java parseModel(XMLStreamReader reader, ParseContext context) {
        return null;
    }

    @Override
    public QName getQname() {
        return Java.type;
    }

    @Override
    public Class<Java> getModelType() {
        return Java.class;
    }
}
