package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event;

import javax.xml.namespace.QName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:51:07 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EndEvent extends AbstractEvent {

    public final static QName type = new QName(BpmnBase.NAME_SPACE, "endEvent");

    private static final long serialVersionUID = 3529827842581828898L;

}
