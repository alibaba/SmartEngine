package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.LoopCardinality;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXXmlParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXXmlParser;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class LoopCardinalityParser extends AbstractStAXXmlParser<LoopCardinality>
    implements StAXXmlParser<LoopCardinality> {

    public LoopCardinalityParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return LoopCardinality.type;
    }

    @Override
    public Class<LoopCardinality> getModelType() {
        return LoopCardinality.class;
    }

    @Override
    public LoopCardinality parse(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        LoopCardinality loopCardinality = new LoopCardinality();

        String expressionType = this.getString(reader, "type");
        String content = reader.getElementText();
        if (null != content) {
            ConditionExpression expression = new ConditionExpression();
            expression.setExpressionContent(content);
            expression.setExpressionType(expressionType);
            loopCardinality.setCardinalityExpression(expression);
        }
        return loopCardinality;
    }
}
