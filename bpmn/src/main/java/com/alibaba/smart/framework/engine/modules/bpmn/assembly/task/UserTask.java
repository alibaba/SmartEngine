package com.alibaba.smart.framework.engine.modules.bpmn.assembly.task;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.BpmnBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.xml.namespace.QName;

/**
 * @author 高海军 帝奇 Apr 14, 2016 2:50:20 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserTask extends AbstractTask {

    private static final long serialVersionUID = 2241766485621486315L;


    public final static QName type = new QName(BpmnBase.NAME_SPACE, "userTask");


    private String name;


}