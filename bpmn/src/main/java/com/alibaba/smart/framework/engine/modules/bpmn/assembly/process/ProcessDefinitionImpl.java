package com.alibaba.smart.framework.engine.modules.bpmn.assembly.process;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractProcessDefinition;
import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
public class ProcessDefinitionImpl extends AbstractProcessDefinition {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "definitions");

    private static final long serialVersionUID = -7973338663278156625L;

    @Override
    public String toString() {
        return super.getId()+":"+super.getVersion();
    }

}
