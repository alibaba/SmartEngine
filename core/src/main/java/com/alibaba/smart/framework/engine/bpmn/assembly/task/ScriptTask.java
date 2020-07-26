package com.alibaba.smart.framework.engine.bpmn.assembly.task;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractTask;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zilong.jiangzl 2020-07-17
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ScriptTask extends AbstractTask {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "scriptTask");
    private static final long serialVersionUID = 2155118199015424897L;

    @Override
    public String toString() {
        return super.getId();
    }

}
