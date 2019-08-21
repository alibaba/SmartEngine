package com.alibaba.smart.framework.engine.modules.smart.assembly.extension.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.smart.assembly.extension.Value;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class ValueParser extends AbstractElementParser<Value> {

    public ValueParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    protected Value parseModel(XMLStreamReader reader, ParseContext context) {
        Value value = new Value();
        value.setName(XmlParseUtil.getString(reader, "name"));
        value.setValue(XmlParseUtil.getString(reader, "value"));
        return value;
    }


    @Override
    public QName getArtifactType() {
        return Value.type;
    }

    @Override
    public Class<Value> getModelType() {
        return Value.class;
    }
}
