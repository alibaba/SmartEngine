package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.exception.EngineException;
import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.LoopCardinality;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER, bindingTo = LoopCardinality.class)

public class LoopCardinalityParser extends AbstractElementParser<LoopCardinality>
      {



    @Override
    public QName getQname() {
        return LoopCardinality.type;
    }

    @Override
    public Class<LoopCardinality> getModelType() {
        return LoopCardinality.class;
    }

    @Override
    public LoopCardinality parseElement(XMLStreamReader reader, ParseContext context) {

        //TUNE 父类

        throw new EngineException("Not supported");

        //LoopCardinality loopCardinality = new LoopCardinality();
        //
        //String expressionType = XmlParseUtil.getString(reader, "type");
        //String content = reader.getElementText();
        //if (null != content) {
        //    ConditionExpression expression = new ConditionExpression();
        //    expression.setExpressionContent(content);
        //    expression.setExpressionType(expressionType);
        //    loopCardinality.setCardinalityExpression(expression);
        //}
        //return loopCardinality;
    }
}
