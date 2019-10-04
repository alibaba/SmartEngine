package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extension.annoation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.CompletionCondition;
import com.alibaba.smart.framework.engine.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.AbstractElementParser;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:26.
 */
@ExtensionBinding(type = ExtensionConstant.ELEMENT_PARSER, bindingTo = CompletionCondition.class)
public class CompletionConditionParser extends AbstractElementParser<CompletionCondition>
{


    @Override
    public QName getQname() {
        return CompletionCondition.type;
    }

    @Override
    public Class<CompletionCondition> getModelType() {
        return CompletionCondition.class;
    }

    @Override
    public CompletionCondition parseElement(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        CompletionCondition completionCondition = new CompletionCondition();

        String expressionType = XmlParseUtil.getString(reader, "type");
        String action = XmlParseUtil.getString(reader, "action");
        if(null==action || "".equals(action)){
            action=CompletionCondition.ACTION_CONTINUE;
        }
        completionCondition.setAction(action);
        String content = reader.getElementText();
        if (null != content) {
            ConditionExpression expression = new ConditionExpression();
            expression.setExpressionContent(content);
            expression.setExpressionType(expressionType);
            completionCondition.setExpression(expression);
        }
        return completionCondition;
    }
}
