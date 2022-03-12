package com.alibaba.smart.framework.engine.bpmn.assembly.expression.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.expression.ConditionExpressionImpl;
import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = ConditionExpressionImpl.class)
public class ConditionExpressionParser extends AbstractElementParser<ConditionExpressionImpl>
      {

    @Override
    public Class<ConditionExpressionImpl> getModelType() {
        return ConditionExpressionImpl.class;
    }

    @Override
    public ConditionExpressionImpl parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        ConditionExpressionImpl conditionExpression = new ConditionExpressionImpl();

        String type = XmlParseUtil.getString(reader, "group");

        String type0 =  reader.getAttributeValue("xsi", "group");
        String type1 =  reader.getAttributeValue(null, "group");
        String type2 =  reader.getAttributeValue(0);


        String content = reader.getElementText();

        conditionExpression.setExpressionContent(content);

        // XML 解析有bug,这里兼容下。
        if(null != type){
            conditionExpression.setExpressionType(type);
        }else  if(null != type0){
            conditionExpression.setExpressionType(type0);
        }else  if(null != type1){
            conditionExpression.setExpressionType(type1);
        }else  if(null != type2){
            conditionExpression.setExpressionType(type2);
        }


        String finalExpressionType = conditionExpression.getExpressionType();
        if(null == finalExpressionType){
            throw new EngineException("expression type should not be empty for expression content:"+ content);
        }

        return conditionExpression;

    }

}
