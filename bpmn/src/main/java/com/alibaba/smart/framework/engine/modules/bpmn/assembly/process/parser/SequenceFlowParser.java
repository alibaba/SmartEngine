package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.extensionpoint.registry.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.pvm.event.PvmEventConstant;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.exception.ParseException;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

public class SequenceFlowParser extends AbstractBpmnParser<SequenceFlow>   {
    private final static String DEFAULT_ACTION = PvmEventConstant.TRANSITION_EXECUTE.name();

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
    public SequenceFlow parseModel(XMLStreamReader reader, ParseContext context)
        throws ParseException, XMLStreamException {
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId(XmlParseUtil.getString(reader, "id"));
        sequenceFlow.setName(XmlParseUtil.getString(reader, "name"));
        sequenceFlow.setSourceRef(XmlParseUtil.getString(reader, "sourceRef"));
        sequenceFlow.setTargetRef(XmlParseUtil.getString(reader, "targetRef"));
        return sequenceFlow;
    }

    @Override
    protected boolean parseModelChild(SequenceFlow model, BaseElement child) {
        if (child instanceof ConditionExpression) {
            model.setConditionExpression((ConditionExpression)child);
            return true;
        }
        return false;
    }

    @Override
    protected String getDefaultActionName() {
        return DEFAULT_ACTION;
    }
}
