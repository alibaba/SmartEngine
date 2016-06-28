package com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression;

import javax.xml.namespace.QName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;
import com.alibaba.smart.framework.engine.modules.common.assembly.Condition;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConditionExpression extends Condition {

    private static final long serialVersionUID = -6152070683207905381L;
    public final static QName type             = new QName(BpmnBase.NAME_SPACE, "conditionExpression");

    private String            expressionType;
    private String            expressionContent;
}
