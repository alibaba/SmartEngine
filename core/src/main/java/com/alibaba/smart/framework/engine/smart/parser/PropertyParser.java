package com.alibaba.smart.framework.engine.smart.parser;

import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.smart.Property;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = Property.class)
public class PropertyParser extends AbstractElementParser<Property> {

    @Override
    protected Property parseModel(XMLStreamReader reader, ParseContext context) {
        Property property = new Property();

        property.setType(XmlParseUtil.getString(reader, "type"));
        property.setName(XmlParseUtil.getString(reader, "name"));
        property.setValue(XmlParseUtil.getString(reader, "value"));

        Map<String, String> allAttrs = XmlParseUtil.parseExtendedProperties(reader, context);

        property.setAttrs(allAttrs);

        return property;
    }

    @Override
    public Class<Property> getModelType() {
        return Property.class;
    }
}
