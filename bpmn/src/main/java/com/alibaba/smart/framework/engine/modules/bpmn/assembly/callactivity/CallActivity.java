package com.alibaba.smart.framework.engine.modules.bpmn.assembly.callactivity;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.AbstractBpmnActivity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author 高海军 帝奇 74394
 * @date 2017 May  14:30
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class CallActivity extends AbstractBpmnActivity {

    public final static QName type = new QName(BpmnBase.NAME_SPACE, "callActivity");

    private String calledElement;

    private String calledElementVersion;

}
