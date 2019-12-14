package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class LoopCardinality implements NoneIdBasedElement {
    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "loopCardinality");

    private ConditionExpression cardinalityExpression;

    public ConditionExpression getCardinalityExpression() {
        return cardinalityExpression;
    }

    public void setCardinalityExpression(
        ConditionExpression cardinalityExpression) {
        this.cardinalityExpression = cardinalityExpression;
    }
}
