package com.alibaba.smart.framework.engine.modules.bpmn.assembly.action;

import com.alibaba.smart.framework.engine.model.assembly.Script;
import com.alibaba.smart.framework.engine.model.assembly.SmartBase;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractBase;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

/**
 * activity里面可以执行的组件
 * Created by dongdongzdd on 16/9/8.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class Action extends AbstractBase {


    public final static QName NameType             = new QName(BpmnBase.NAME_SPACE, "action");
    private static final long serialVersionUID = 8925386572847689717L;

    private String            type;
    private String            id;
    private String            method;


}
