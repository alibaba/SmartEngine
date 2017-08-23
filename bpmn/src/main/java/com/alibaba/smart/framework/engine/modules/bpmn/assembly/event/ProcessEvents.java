package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnNameSpaceConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * Created by dongdongzdd on 16/9/20.
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessEvents extends AbstractEvent {

    public final static QName NameType = new QName(BpmnNameSpaceConstant.NAME_SPACE, "events");
    private static final long serialVersionUID = 4440927851993238207L;


    private List<ProcessEvent> events;

}
