package com.alibaba.smart.framework.engine.bpmn.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.ConditionExpression;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;

import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:03.
 */

@Data
public class MultiInstanceLoopCharacteristics implements NoneIdBasedElement {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "multiInstanceLoopCharacteristics");

    private boolean sequential;

    private ConditionExpression completionCondition;
    private ConditionExpression abortCondition;

}
