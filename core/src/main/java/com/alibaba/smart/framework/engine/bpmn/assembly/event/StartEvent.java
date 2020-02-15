package com.alibaba.smart.framework.engine.bpmn.assembly.event;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StartEvent extends AbstractEvent {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "startEvent");

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
