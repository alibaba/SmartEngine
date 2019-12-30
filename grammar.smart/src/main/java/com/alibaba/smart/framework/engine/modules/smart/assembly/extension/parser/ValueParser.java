package com.alibaba.smart.framework.engine.modules.smart.assembly.extension.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Value;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = Value.class)

public class ValueParser extends AbstractElementParser<Value> {



    @Override
    protected Value parseModel(XMLStreamReader reader, ParseContext context) {
        Value value = new Value();
        value.setName(XmlParseUtil.getString(reader, "name"));
        value.setValue(XmlParseUtil.getString(reader, "value"));
        return value;
    }


    @Override
    public QName getQname() {
        return Value.type;
    }

    @Override
    public Class<Value> getModelType() {
        return Value.class;
    }
}
