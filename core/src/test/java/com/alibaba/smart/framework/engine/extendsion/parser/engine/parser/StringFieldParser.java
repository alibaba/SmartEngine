package com.alibaba.smart.framework.engine.extendsion.parser.engine.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extendsion.parser.engine.StringField;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zilong.jiangzl
 * @create 2020-07-16 9:40 下午
 */
@Slf4j
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = StringField.class)
public class StringFieldParser extends AbstractElementParser<StringField> {
    static int count = 0;
    @Override
    protected StringField parseModel(XMLStreamReader reader, ParseContext context) {
        System.out.println("StringFieldParser1 " + reader.getEventType());

        StringField value = new StringField();

        try {
            //TODO
            // getElementText会把 reader的START_ELEMENT ==> END_ELEMENT，导致框架的后续解析发生异常
            // 后续框架的 int attributeCount = reader.getAttributeCount();会抛异常
            value.setValue(reader.getElementText());
        } catch (XMLStreamException e) {
            log.error("StringFieldParser.parseModel", e);
        }
        count++;
        //TODO FIXME
       value.setValue("count" + count + ".");
       System.out.println("StringFieldParser2 " + reader.getEventType());

        return value;
    }

    @Override
    public QName getQname() {
        return StringField.type;
    }

    @Override
    public Class<StringField> getModelType() {
        return StringField.class;
    }
}
