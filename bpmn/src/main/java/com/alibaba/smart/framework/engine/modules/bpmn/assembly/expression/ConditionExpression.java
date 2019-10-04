package com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@Data
public class ConditionExpression  implements
    com.alibaba.smart.framework.engine.model.assembly.ConditionExpression {

    //TUNE 命名

    private static final long serialVersionUID = -6152070683207905381L;
    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "conditionExpression");

    private String expressionType;
    private String expressionContent;
}
