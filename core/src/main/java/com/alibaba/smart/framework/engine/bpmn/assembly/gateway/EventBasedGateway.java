package com.alibaba.smart.framework.engine.bpmn.assembly.gateway;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class EventBasedGateway extends AbstractGateway {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "eventBasedGateway");

    private static final long serialVersionUID = 7631298475012839456L;

    @Override
    public String toString() {
        return super.getId();
    }

}
