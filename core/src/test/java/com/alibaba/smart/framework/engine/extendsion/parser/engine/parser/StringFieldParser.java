package com.alibaba.smart.framework.engine.extendsion.parser.engine.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.expression.ConditionExpressionImpl;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extendsion.parser.engine.StringField;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zilong.jiangzl
 * @create 2020-07-16 9:40 下午
 */
@Slf4j
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = StringField.class)
public class StringFieldParser extends AbstractElementParser<StringField> {



    @Override
    public StringField parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        StringField field = new StringField();

        try {

            String elementText = reader.getElementText();
            field.setValue(elementText);


        } catch (Exception e) {
            throw  new EngineException(e);
        }


        return field;

    }

    @Override
    public Class<StringField> getModelType() {
        return StringField.class;
    }
}
