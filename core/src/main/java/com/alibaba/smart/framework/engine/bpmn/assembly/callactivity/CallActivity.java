package com.alibaba.smart.framework.engine.bpmn.assembly.callactivity;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractActivity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

/**
 * @author 高海军 帝奇 74394
 * @date 2017 May 14:30
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CallActivity extends AbstractActivity {

    public static final QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "callActivity");

    private String calledElement;

    private String calledElementVersion;
}
