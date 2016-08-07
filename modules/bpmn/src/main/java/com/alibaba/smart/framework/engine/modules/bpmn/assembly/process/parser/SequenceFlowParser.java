package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.Handler;
import com.alibaba.smart.framework.engine.assembly.parser.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parser.exception.ParseException;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension.ExtensionElements;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;

public class SequenceFlowParser extends AbstractBpmnParser<SequenceFlow> implements StAXArtifactParser<SequenceFlow> {

    public SequenceFlowParser(ExtensionPointRegistry extensionPointRegistry) {
        super(extensionPointRegistry);
    }

    @Override
    public QName getArtifactType() {
        return SequenceFlow.type;
    }

    @Override
    public Class<SequenceFlow> getModelType() {
        return SequenceFlow.class;
    }

    @Override
    public SequenceFlow parse(XMLStreamReader reader, ParseContext context) throws ParseException, XMLStreamException {
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId(this.getString(reader, "id"));
        sequenceFlow.setSourceRef(this.getString(reader, "sourceRef"));
        sequenceFlow.setTargetRef(this.getString(reader, "targetRef"));

        this.parseChildren(sequenceFlow, reader, context);
        return sequenceFlow;
    }

    @Override
    protected void parseChild(SequenceFlow model, BaseElement child) {
        if (child instanceof ConditionExpression) {
            model.setConditionExpression((ConditionExpression) child);
        } else if (child instanceof Handler) {
            model.setHandler((Handler) child);
        } else if (child instanceof ExtensionElements) {
            model.setExtensions((ExtensionElements) child);
        }
    }
}
