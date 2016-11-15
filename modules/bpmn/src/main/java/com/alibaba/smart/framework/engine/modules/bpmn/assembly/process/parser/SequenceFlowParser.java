package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension.ExtensionElements;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.xml.parser.exception.ParseException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

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
        }  else if (child instanceof ExtensionElements) {
            model.setExtensions((ExtensionElements) child);
        }
    }
}
