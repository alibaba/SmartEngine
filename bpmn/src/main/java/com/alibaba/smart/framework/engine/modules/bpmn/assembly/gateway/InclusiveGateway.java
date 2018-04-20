package com.alibaba.smart.framework.engine.modules.bpmn.assembly.gateway;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by niefeng on 2018/4/18.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InclusiveGateway extends AbstractGateway {
    public final static QName type = new QName(BpmnNameSpaceConstant.NAME_SPACE, "inclusiveGateway");
    /**
     *
     */
    private static final long serialVersionUID = 5754815434231251702L;



    @Override
    public String toString() {
        return super.getId();
    }
}
