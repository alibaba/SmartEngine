package com.alibaba.smart.framework.engine.modules.bpmn.assembly.diagram;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractProcess;
import com.alibaba.smart.framework.engine.constant.BpmnNameSpaceConstant;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  10:02.
 */
public class Diagram extends AbstractProcess {

    public final static QName type = new QName(BpmnNameSpaceConstant.BPMNDI_NAME_SPACE, "BPMNDiagram");

    private static final long serialVersionUID = -2660788294142169268L;

}
