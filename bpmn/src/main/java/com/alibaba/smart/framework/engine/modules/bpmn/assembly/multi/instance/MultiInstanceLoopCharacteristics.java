package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.ExecutePolicy;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractElement;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:03.
 */

@Data
public class MultiInstanceLoopCharacteristics extends AbstractElement implements ExecutePolicy{

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "multiInstanceLoopCharacteristics");

    private String elementVariable;

    private boolean isSequential;

    private CompletionCondition completionCondition;

    private CollectionCondition collectionCondition;
}
