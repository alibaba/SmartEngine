package com.alibaba.smart.framework.engine.modules.bpmn.assembly.common;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractProcess;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  10:02.
 */
public class Incoming extends AbstractProcess {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "incoming");

    private static final long serialVersionUID = -2660788294142169268L;

}
