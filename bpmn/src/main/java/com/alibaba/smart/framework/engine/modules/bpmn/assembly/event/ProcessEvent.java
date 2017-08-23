package com.alibaba.smart.framework.engine.modules.bpmn.assembly.event;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnNameSpaceConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

/**
 * Created by dongdongzdd on 16/9/20.
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessEvent extends AbstractEvent {

    public final static QName NameType = new QName(BpmnNameSpaceConstant.NAME_SPACE, "event");
    private static final long serialVersionUID = 8077655548367028036L;


    private String type;
    private String id;
    private String method;

}
