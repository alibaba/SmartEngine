package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractTransition;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇 Apr 21, 2016 3:08:26 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SequenceFlow extends AbstractTransition {

    /**
     *
     */
    private static final long serialVersionUID = 664248469321447390L;
    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "sequenceFlow");

    private ConditionExpression conditionExpression;


    @Override
    public String toString() {
        return super.getSourceRef() + " --> "+ super.getId() +" --> " + super.getTargetRef();
    }
}
