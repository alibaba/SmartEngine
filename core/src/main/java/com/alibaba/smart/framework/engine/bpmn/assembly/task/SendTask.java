package com.alibaba.smart.framework.engine.bpmn.assembly.task;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractTask;

/**
 * @author zilong.jiangzl 2020-07-17
 */
public class SendTask extends AbstractTask {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "sendTask");
    private static final long serialVersionUID = 4322119220754998002L;

    @Override
    public String toString() {
        return super.getId();
    }

}
