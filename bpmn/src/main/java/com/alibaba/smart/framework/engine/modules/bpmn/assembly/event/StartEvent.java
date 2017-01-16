package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StartEvent extends AbstractEvent {

    public final static QName type = new QName(BpmnBase.NAME_SPACE, "startEvent");

    private static final long serialVersionUID = 8769494440379002970L;

    @Override
    public boolean isStartActivity() {
        return true;
    }

    @Override
    public String toString() {
        return super.getId();
    }
}
