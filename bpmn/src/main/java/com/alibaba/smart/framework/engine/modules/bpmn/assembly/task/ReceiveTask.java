package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReceiveTask extends AbstractTask {

    /**
     *
     */
    private static final long serialVersionUID = 5926063576480176197L;


    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "receiveTask");

    @Override
    public String toString() {
        return super.getId();
    }

}
