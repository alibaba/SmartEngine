package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractBaseElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class LoopCardinality extends AbstractBaseElement implements LoopCollection {
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
