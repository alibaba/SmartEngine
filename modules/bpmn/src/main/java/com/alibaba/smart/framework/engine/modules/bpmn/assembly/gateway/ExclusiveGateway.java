package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway;

import javax.xml.namespace.QName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */

@Data
@EqualsAndHashCode(callSuper = true)

public class ExclusiveGateway extends AbstractGateway {
    public final static QName type = new QName(BpmnBase.NAME_SPACE, "exclusiveGateway");
    /**
     * 
     */
    private static final long serialVersionUID = 5754815434014251702L;

}
