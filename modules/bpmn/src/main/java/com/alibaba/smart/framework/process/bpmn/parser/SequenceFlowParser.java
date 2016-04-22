package com.alibaba.smart.framework.process.bpmn.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.parse.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parse.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parse.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.process.model.bpmn.assembly.activity.SequenceFlow;
import com.alibaba.smart.framework.process.model.bpmn.assembly.gateway.ConditionExpression;

public class SequenceFlowParser extends AbstractStAXArtifactParser<SequenceFlow> implements StAXArtifactParser<SequenceFlow> {

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
        //TODO 方法命名待改善 ,当发现没有parser时或者扩展点时,应该报错
        sequenceFlow.setSourceRef(this.getString(reader, "sourceRef"));
        sequenceFlow.setTargetRef(this.getString(reader, "targetRef"));

        while (this.nextChildElement(reader)) {
            Object element = this.readElement(reader, context);
            //TODO 当条件为空时,设置默认的条件
            if (element instanceof ConditionExpression) {
                ConditionExpression conditionExpression = (ConditionExpression) element;
                sequenceFlow.setConditionExpression(conditionExpression);
            }
        }

        return sequenceFlow;
    }

}
