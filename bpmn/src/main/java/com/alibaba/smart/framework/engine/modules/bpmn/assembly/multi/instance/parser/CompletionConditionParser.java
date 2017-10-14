package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.CompletionCondition;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.parser.impl.AbstractStAXArtifactParser;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:26.
 */
public class CompletionConditionParser extends AbstractStAXArtifactParser<CompletionCondition>
    implements StAXArtifactParser<CompletionCondition> {

    public CompletionConditionParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return CompletionCondition.type;
    }

    @Override
    public Class<CompletionCondition> getModelType() {
        return CompletionCondition.class;
    }

    @Override
    public CompletionCondition parse(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        CompletionCondition completionCondition = new CompletionCondition();

        String content = reader.getElementText();

        completionCondition.setExpressionContent(content);

        return completionCondition;
    }
}
