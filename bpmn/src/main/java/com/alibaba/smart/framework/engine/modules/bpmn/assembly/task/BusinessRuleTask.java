package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

/**
 * ${DESCRIPTION}
 *
 * @author zilong.jiangzl
 * @create 2019-05-21 下午9:09
 */
public class BusinessRuleTask extends AbstractTask {

    /**
     *
     */
    private static final long serialVersionUID = 5926063576480176197L;


    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "businessRuleTask");

    @Override
    public String toString() {
        return super.getId();
    }

}

