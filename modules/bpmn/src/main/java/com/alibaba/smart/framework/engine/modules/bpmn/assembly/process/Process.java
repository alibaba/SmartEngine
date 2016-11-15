package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractProcess;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;

import javax.xml.namespace.QName;

/**
 * @author 高海军 帝奇 Apr 21, 2016 1:37:18 PM TODO 这个需要考虑下,process 自身不是一个activity,但是subprocess 被当成一个activity
 */
public class Process extends AbstractProcess {

    public final static QName type = new QName(BpmnBase.NAME_SPACE, "process");

    private static final long serialVersionUID = -2660788294142169268L;

}
