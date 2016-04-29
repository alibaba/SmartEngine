package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceTask extends AbstractTask {

    public final static QName type             = new QName(BpmnBase.NAME_SPACE, "serviceTask");

    /**
     * 
     */
    private static final long serialVersionUID = 2900871220232200586L;
}
