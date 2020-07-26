package com.alibaba.smart.framework.engine.bpmn.assembly.task;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractTask;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReceiveTask extends AbstractTask {

    /**
     *
     */
    private static final long serialVersionUID = 5926063576480176197L;


    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "receiveTask");

    @Override
    public String toString() {
        return super.getId();
    }

}
