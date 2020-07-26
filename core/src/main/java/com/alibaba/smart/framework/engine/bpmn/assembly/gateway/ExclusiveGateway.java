package com.alibaba.smart.framework.engine.bpmn.assembly.gateway;
import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ExclusiveGateway extends AbstractGateway {

    public final static QName qtype = new QName(BpmnNameSpaceConstant.NAME_SPACE, "exclusiveGateway");
    /**
     *
     */
    private static final long serialVersionUID = 5754815434014251702L;

    @Override
    public String toString() {
        return super.getId();
    }

}
