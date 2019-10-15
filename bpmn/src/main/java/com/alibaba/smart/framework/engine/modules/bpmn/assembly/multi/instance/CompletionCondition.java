package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:27.
 */

@Data
public class CompletionCondition implements NoneIdBasedElement {
    public final static String ACTION_ABORT="abort";
    public final static String ACTION_CONTINUE="continue";

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "completionCondition");
    private String action;
    private ConditionExpression expression;
}
