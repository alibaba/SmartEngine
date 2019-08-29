package com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.extensionpoint.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.CompletionCondition;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;
@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER,binding = ConditionExpression.class)
public class ConditionExpressionParser extends AbstractElementParser<ConditionExpression>
      {



    @Override
    public QName getQname() {
        return ConditionExpression.type;
    }

    @Override
    public Class<ConditionExpression> getModelType() {
        return ConditionExpression.class;
    }

    @Override
    public ConditionExpression parseElement(XMLStreamReader reader, ParseContext context) throws ParseException,
            XMLStreamException {
        ConditionExpression conditionExpression = new ConditionExpression();

        String type = XmlParseUtil.getString(reader, "type");

        String type0 =  reader.getAttributeValue("xsi", "type");
        String type1 =  reader.getAttributeValue(null, "type");
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
            throw new EngineException("type should not be empty for expression content:"+ finalExpressionType);
        }

//        Script script = new Script();
//        script.setType(type);
//        script.setContent(content);
//        conditionExpression.setHandler(script);
        // this.skipToEndElement(reader);
        return conditionExpression;

    }

}
