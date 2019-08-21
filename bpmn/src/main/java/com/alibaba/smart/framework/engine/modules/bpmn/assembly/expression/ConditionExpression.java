package com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression;

import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractPerformable;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConditionExpression extends AbstractPerformable implements Performable {

    private static final long serialVersionUID = -6152070683207905381L;
    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "conditionExpression");

    private String expressionType;
    private String expressionContent;
}
