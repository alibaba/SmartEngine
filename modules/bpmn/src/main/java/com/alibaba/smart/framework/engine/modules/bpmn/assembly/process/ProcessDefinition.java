package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractProcessDefinition;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;

import javax.xml.namespace.QName;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
public class ProcessDefinition extends AbstractProcessDefinition {

    public final static QName type = new QName(BpmnBase.NAME_SPACE, "definitions");

    private static final long serialVersionUID = -7973338663278156625L;

}
