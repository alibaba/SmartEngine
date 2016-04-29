package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.assembly.Base;
import com.alibaba.smart.framework.engine.assembly.Handler;
import com.alibaba.smart.framework.engine.assembly.parse.ParseContext;
import com.alibaba.smart.framework.engine.assembly.parser.StAXArtifactParser;
import com.alibaba.smart.framework.engine.assembly.parse.exception.ParseException;
import com.alibaba.smart.framework.engine.assembly.parser.impl.AbstractStAXArtifactParser;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension.ExtensionElements;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;

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
        //TODO 方法命名待改善 ,当发现没有parser时或者扩展点时,应该报错
        sequenceFlow.setSourceRef(this.getString(reader, "sourceRef"));
        sequenceFlow.setTargetRef(this.getString(reader, "targetRef"));

        this.parseChildren(sequenceFlow,reader,context);
        return sequenceFlow;
    }

    @Override
    protected void parseChild(SequenceFlow model, Base child) {
        if(child instanceof ConditionExpression) {
            model.setConditionExpression((ConditionExpression)child);
        }else if(child instanceof Handler) {
            model.setHandler((Handler)child);
        }else if(child instanceof ExtensionElements){
            model.setExtensions((ExtensionElements)child);
        }
    }
}
