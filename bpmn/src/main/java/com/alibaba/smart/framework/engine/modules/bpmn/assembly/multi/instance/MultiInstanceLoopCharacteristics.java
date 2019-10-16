package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.expression.ConditionExpression;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:03.
 */

@Data
public class MultiInstanceLoopCharacteristics implements NoneIdBasedElement {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "multiInstanceLoopCharacteristics");


    private ConditionExpression completionCondition;
    private ConditionExpression abortCondition;

}
