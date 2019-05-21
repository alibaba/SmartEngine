package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

/**
 * ${DESCRIPTION}
 *
 * @author zilong.jiangzl
 * @create 2019-05-21 下午8:57
 */
public class InclusiveGateway extends AbstractGateway {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "inclusiveGateway");

    private static final long serialVersionUID = 2096892588768688904L;

    @Override
    public String toString() {
        return super.getId();
    }

}