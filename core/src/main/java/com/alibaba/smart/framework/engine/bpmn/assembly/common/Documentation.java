package com.alibaba.smart.framework.engine.bpmn.assembly.common;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;

import javax.xml.namespace.QName;

/** Created by 高海军 帝奇 74394 on 2017 August 10:02. */
public class Documentation implements NoneIdBasedElement {

    public static final QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "documentation");

    private static final long serialVersionUID = -2660788294142169268L;
}
