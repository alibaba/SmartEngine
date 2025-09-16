package com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;

import lombok.Data;

import javax.xml.namespace.QName;

/** Created by 高海军 帝奇 74394 on 2017 September 21:27. */
@Data
public class CompletionCondition implements NoneIdBasedElement {
    public static final String ACTION_ABORT = "abort";
    public static final String ACTION_CONTINUE = "continue";

    public static final QName qtype =
            new QName(BpmnNameSpaceConstant.NAME_SPACE, "completionCondition");
    private String action;
    private ConditionExpression expression;
}
