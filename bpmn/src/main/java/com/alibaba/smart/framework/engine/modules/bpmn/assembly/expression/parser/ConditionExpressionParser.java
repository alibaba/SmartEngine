package com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.parser;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.xml.parser.ElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ConditionExpressionParser extends AbstractElementParser<ConditionExpression>
      {

    public ConditionExpressionParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
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

        String type = getString(reader, "type");

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
