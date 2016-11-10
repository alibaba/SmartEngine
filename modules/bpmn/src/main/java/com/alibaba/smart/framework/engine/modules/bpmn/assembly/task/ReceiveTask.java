package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReceiveTask extends AbstractTask {

    /**
     * 
     */
    private static final long serialVersionUID = 5926063576480176197L;


    public final static QName type             = new QName(BpmnBase.NAME_SPACE, "receiveTask");

    private String className;

}
