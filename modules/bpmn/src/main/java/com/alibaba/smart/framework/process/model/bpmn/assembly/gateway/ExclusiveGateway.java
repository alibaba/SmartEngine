package com.alibaba.smart.framework.process.model.bpmn.assembly.gateway;

import javax.xml.namespace.QName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.alibaba.smart.framework.process.model.bpmn.NameSpaceConstant;
import com.alibaba.smart.framework.process.model.bpmn.assembly.activity.ProcessActivity;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */

@Data
@EqualsAndHashCode(callSuper = true)

public class ExclusiveGateway  extends ProcessActivity{

    public final static QName type = new QName(NameSpaceConstant.NAME_SPACE,"exclusiveGateway");

    /**
     * 
     */
    private static final long serialVersionUID = 5754815434014251702L;
    
    private String name;

}
