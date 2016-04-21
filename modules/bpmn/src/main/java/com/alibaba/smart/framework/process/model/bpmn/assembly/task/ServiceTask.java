package com.alibaba.smart.framework.process.model.bpmn.assembly.task;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.process.model.bpmn.NameSpaceConstant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceTask extends Task {

    public final static QName type             = new QName(NameSpaceConstant.NAME_SPACE, "serviceTask");

    /**
     * 
     */
    private static final long serialVersionUID = 2900871220232200586L;
    /**
     * 目前该属性用处不大,但是不排除未来作为扩展性用途
     */
    protected String          implementationType;
    protected String          implementation;
}
