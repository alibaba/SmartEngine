package com.alibaba.smart.framework.engine.bpmn.assembly.event;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class IntermediateCatchEvent extends AbstractEvent {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "intermediateCatchEvent");

    private static final long serialVersionUID = 4528917365201847392L;

    @Override
    public String toString() {
        return super.getId();
    }
}
