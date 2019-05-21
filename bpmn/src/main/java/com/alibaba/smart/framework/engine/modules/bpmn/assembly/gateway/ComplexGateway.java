package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

/**
 * ${DESCRIPTION}
 *
 * @author zilong.jiangzl
 * @create 2019-05-21 下午8:57
 */
public class ComplexGateway extends AbstractGateway {

    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "complexGateway");
    /**
     *
     */
    private static final long serialVersionUID = 5754815434014251704L;

    @Override
    public String toString() {
        return super.getId();
    }

}
