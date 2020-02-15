package com.alibaba.smart.framework.engine.bpmn.assembly.diagram;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  10:02.
 */
public class Diagram implements NoneIdBasedElement {

    public final static QName type = new QName(BpmnNameSpaceConstant.BPMNDI_NAME_SPACE, "BPMNDiagram");

    private static final long serialVersionUID = -2660788294142169268L;

}
