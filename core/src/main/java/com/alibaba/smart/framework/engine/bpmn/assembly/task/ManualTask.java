package com.alibaba.smart.framework.engine.bpmn.assembly.task;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractTask;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

/**
 * @author zilong.jiangzl 2020-07-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ManualTask extends AbstractTask {

    public static final QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "manualTask");
    private static final long serialVersionUID = 2130902428481945847L;

    @Override
    public String toString() {
        return super.getId();
    }
}
