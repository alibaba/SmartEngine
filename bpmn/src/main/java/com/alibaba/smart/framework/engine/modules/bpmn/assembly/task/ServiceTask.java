package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.action.Action;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.event.ProcessEvents;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceTask extends AbstractTask {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "serviceTask");
    private static final long serialVersionUID = 2900871220232200586L;

    /**
     * 流程节点的主要执行动作
     */
    private Action action;

    /**
     * 流程节点中可以发生的事件
     */
    private ProcessEvents events;

    private String className;


    @Override
    public String toString() {
        return super.getId();
    }

}
