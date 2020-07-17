package com.alibaba.smart.framework.engine.extendsion.parser.engine.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extendsion.parser.engine.ProcessField;
import com.alibaba.smart.framework.engine.extendsion.parser.engine.StringField;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author zilong.jiangzl
 * @create 2020-07-16 9:40 下午
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ProcessField.class)
public class ProcessFieldParser extends AbstractElementParser<ProcessField> {

    @Override
    protected ProcessField parseModel(XMLStreamReader reader, ParseContext context) {
        ProcessField value = new ProcessField();
        System.out.println("ProcessFieldParse1 " + reader.getEventType());
        value.setName(XmlParseUtil.getString(reader, "name"));
        value.setValue(XmlParseUtil.getString(reader, "stringValue"));
        if (value.getValue() != null) {
            value.setValueType("string");
            return value;
        }
        System.out.println("ProcessFieldParse2 " + reader.getEventType());

        return value;
    }

    @Override
    protected void decorateChild(ProcessField model, BaseElement child) {
        if (child instanceof Extension) {
            if (child instanceof StringField) {
                model.setValue(((StringField)child).getValue());
                model.setValueType("string");
            }
        }
    }



    @Override
    public QName getQname() {
        return ProcessField.type;
    }

    @Override
    public Class<ProcessField> getModelType() {
        return ProcessField.class;
    }
}
