package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractBaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:27.
 */

@Data
@EqualsAndHashCode(callSuper = true)

public class CompletionCondition extends AbstractBaseElement {
    public final static String ACTION_ABORT="abort";
    public final static String ACTION_CONTINUE="continue";

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "completionCondition");
    private String action;
    private ConditionExpression expression;
}
