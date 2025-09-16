package com.alibaba.smart.framework.engine.bpmn.assembly.gateway;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

/**
 * @author 高海军 帝奇 Apr 24, 2024 09:25:01 AM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InclusiveGateway extends AbstractGateway {

    public static final QName qtype =
            new QName(BpmnNameSpaceConstant.NAME_SPACE, "inclusiveGateway");

    /** */
    private static final long serialVersionUID = 5754815434014251702L;

    @Override
    public String toString() {
        return super.getId();
    }
}
