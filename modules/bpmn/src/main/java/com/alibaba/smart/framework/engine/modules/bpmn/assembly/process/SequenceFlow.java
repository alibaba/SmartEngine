package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractTransition;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.extension.ExtensionElements;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

/**
 * @author 高海军 帝奇 Apr 21, 2016 3:08:26 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SequenceFlow extends AbstractTransition {

    /**
     *
     */
    private static final long   serialVersionUID = 664248469321447390L;
    public final static QName   type             = new QName(BpmnBase.NAME_SPACE, "sequenceFlow");

    private ConditionExpression conditionExpression;
    private ExtensionElements   extensions;

}
