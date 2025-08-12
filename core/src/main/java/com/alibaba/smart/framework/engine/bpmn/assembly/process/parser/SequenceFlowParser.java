package com.alibaba.smart.framework.engine.bpmn.assembly.process.parser;

import javax.xml.stream.XMLStreamReader;

import com.alibaba.smart.framework.engine.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.alibaba.smart.framework.engine.extension.constant.ExtensionConstant;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.xml.parser.ParseContext;
import com.alibaba.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = SequenceFlow.class)

public class SequenceFlowParser extends AbstractBpmnParser<SequenceFlow>   {

    @Override
    public Class<SequenceFlow> getModelType() {
        return SequenceFlow.class;
    }

    @Override
    public SequenceFlow parseModel(XMLStreamReader reader, ParseContext context) {
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId(XmlParseUtil.getString(reader, "id"));
        sequenceFlow.setName(XmlParseUtil.getString(reader, "name"));
        sequenceFlow.setSourceRef(XmlParseUtil.getString(reader, "sourceRef"));
        sequenceFlow.setTargetRef(XmlParseUtil.getString(reader, "targetRef"));

        //Map<String, String> properties = super.parseExtendedProperties(reader,  context);
        //sequenceFlow.setProperties(properties);

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

}
