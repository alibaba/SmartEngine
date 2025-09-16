package com.alibaba.smart.framework.engine.bpmn.assembly.process;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractTransition;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

import javax.xml.namespace.QName;

/**
 * @author 高海军 帝奇 Apr 21, 2016 3:08:26 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SequenceFlow extends AbstractTransition {

    /** */
    private static final long serialVersionUID = 664248469321447390L;

    public static final QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "sequenceFlow");

    private ConditionExpression conditionExpression;

    private Map<String, String> properties;

    @Override
    public String toString() {
        return super.getSourceRef() + " --> " + super.getId() + " --> " + super.getTargetRef();
    }
}
