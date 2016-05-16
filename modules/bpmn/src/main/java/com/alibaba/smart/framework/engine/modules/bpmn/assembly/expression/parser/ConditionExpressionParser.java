package com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.Script;
import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parser.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;

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
        ConditionExpression conditionExpression = new ConditionExpression();
        Script script=new Script();
        script.setType(getString(reader,"type"));
        script.setContent(reader.getElementText());
        conditionExpression.setHandler(script);
        //this.skipToEndElement(reader);
        return conditionExpression;

    }

  

}
