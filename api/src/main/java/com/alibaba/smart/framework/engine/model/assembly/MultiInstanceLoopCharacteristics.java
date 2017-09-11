package com.alibaba.smart.framework.engine.model.assembly;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractElement;

import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:03.
 */

@Data
public class MultiInstanceLoopCharacteristics extends AbstractElement {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "multiInstanceLoopCharacteristics");

    private  CompletionCondition completionCondition;
}
