package com.alibaba.smart.framework.engine.modules.bpmn.assembly.common;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  10:02.
 */
public class Outgoing implements NoneIdBasedElement {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "outgoing");

    private static final long serialVersionUID = -2660788294142169268L;

}
