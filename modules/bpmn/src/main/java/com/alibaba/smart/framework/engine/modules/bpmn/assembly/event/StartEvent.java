package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event;

import javax.xml.namespace.QName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM TODO 有些sid编译告警.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StartEvent extends AbstractEvent {

    public final static QName type             = new QName(BpmnBase.NAME_SPACE, "startEvent");

    private static final long serialVersionUID = 8769494440379002970L;

    @Override
    public boolean isStartActivity() {
        return true;
    }

}
