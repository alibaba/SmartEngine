package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ParallelGateway extends AbstractGateway {

    private static final long serialVersionUID = 4234776128556310813L;

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "parallelGateway");

    @Override
    public String toString() {
        return super.getId();
    }

}
