package com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;

import javax.xml.namespace.QName;

/**
 * @author ettear Created by ettear on 15/10/2017.
 */
public class LoopCardinality implements NoneIdBasedElement {
    public static final QName qtype =
            new QName(BpmnNameSpaceConstant.NAME_SPACE, "loopCardinality");

    private ConditionExpression cardinalityExpression;

    public ConditionExpression getCardinalityExpression() {
        return cardinalityExpression;
    }

    public void setCardinalityExpression(ConditionExpression cardinalityExpression) {
        this.cardinalityExpression = cardinalityExpression;
    }
}
