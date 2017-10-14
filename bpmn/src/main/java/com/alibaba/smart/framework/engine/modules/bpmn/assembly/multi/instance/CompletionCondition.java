package com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractElement;

import lombok.Data;

/**
 * Created by 高海军 帝奇 74394 on 2017 September  21:27.
 */

@Data
public class CompletionCondition extends AbstractElement {
    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "completionCondition");

    private String expressionContent;
}
