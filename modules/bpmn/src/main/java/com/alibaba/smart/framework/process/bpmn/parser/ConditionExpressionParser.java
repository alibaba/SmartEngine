package com.alibaba.smart.framework.process.bpmn.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parse.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parse.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parse.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.model.bpmn.assembly.gateway.ConditionExpression;


public class ConditionExpressionParser extends AbstractStAXArtifactParser<ConditionExpression> implements StAXArtifactParser<ConditionExpression>{

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
    public ConditionExpression parse(XMLStreamReader reader, ParseContext context) throws ParseException,
                                                                                  XMLStreamException {
        
//        <conditionExpression xsi:type="tFormalExpression">${input == 1}</conditionExpression>

        ConditionExpression conditionExpression = new ConditionExpression();
        //TODO 指定super all
        conditionExpression.setExpressionType(reader.getAttributeValue("xsi", "type"));
        conditionExpression.setExpressionContent(reader.getElementText());

        return conditionExpression;
    }

  

}
