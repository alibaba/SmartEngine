package com.alibaba.smart.framework.engine.modules.bpmn.assembly.callactivity;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractActivity;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author 高海军 帝奇 74394
 * @date 2017 May  14:30
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class CallActivity extends AbstractActivity {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "callActivity");

    private String calledElement;

    private String calledElementVersion;

}
