package com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class ConditionExpressionParser extends AbstractStAXArtifactParser<ConditionExpression> implements StAXArtifactParser<ConditionExpression> {

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

        String type = getString(reader, "type");
        String content = reader.getElementText();

        conditionExpression.setExpressionContent(content);
        conditionExpression.setExpressionType(type);


//        Script script = new Script();
//        script.setType(type);
//        script.setContent(content);
//        conditionExpression.setHandler(script);
        // this.skipToEndElement(reader);
        return conditionExpression;

    }

}
