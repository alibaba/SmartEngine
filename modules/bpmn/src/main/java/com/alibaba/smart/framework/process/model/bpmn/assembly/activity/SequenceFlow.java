package com.alibaba.smart.framework.process.model.bpmn.assembly.activity;

import javax.xml.namespace.QName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractTransition;
import com.alibaba.smart.framework.process.model.bpmn.NameSpaceConstant;
import com.alibaba.smart.framework.process.model.bpmn.assembly.gateway.ConditionExpression;


/**
 * @author 高海军 帝奇 Apr 21, 2016 3:08:26 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(doNotUseGetters=true)
public class SequenceFlow extends AbstractTransition {

    /**
     * 
     */
    private static final long serialVersionUID = 664248469321447390L;
    public final static QName type = new QName(NameSpaceConstant.NAME_SPACE, "sequenceFlow");
    
    private ConditionExpression conditionExpression;

    
    public ConditionExpression getConditionExpression() {
        return conditionExpression;
    }

    
    public void setConditionExpression(ConditionExpression conditionExpression) {
        this.conditionExpression = conditionExpression;
    }
}
