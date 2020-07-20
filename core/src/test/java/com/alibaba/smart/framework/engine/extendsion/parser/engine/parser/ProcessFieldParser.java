package com.alibaba.smart.framework.engine.extendsion.parser.engine.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.alibaba.smart.framework.engine.extendsion.parser.engine.ProcessField;
import com.alibaba.smart.framework.engine.extendsion.parser.engine.StringField;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * @author zilong.jiangzl
 * @create 2020-07-16 9:40 下午
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ProcessField.class)
public class ProcessFieldParser extends AbstractBpmnParser<ProcessField> {

    @Override
    protected ProcessField parseModel(XMLStreamReader reader, ParseContext context) {
        ProcessField value = new ProcessField();

        value.setName(XmlParseUtil.getString(reader, "name"));
        value.setValue(XmlParseUtil.getString(reader, "stringValue"));
        if (value.getValue() != null) {
            value.setValueType("string");
            return value;
        }

        return value;
    }

    @Override
    protected void decorateChild(ProcessField model, BaseElement child) {
            if (child instanceof StringField) {
                model.setValue(((StringField)child).getValue());
                model.setValueType("string");
            }
    }

    @Override
    protected boolean parseModelChild(ProcessField model, BaseElement child) {
        if (child instanceof StringField) {
            model.setValue(((StringField)child).getValue());
            model.setValueType("string");
            return true;
        }
        return false;
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
