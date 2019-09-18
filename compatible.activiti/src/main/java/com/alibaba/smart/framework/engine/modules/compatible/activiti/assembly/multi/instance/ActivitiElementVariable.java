package com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.InputDataItem;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.ActivitiBase;

/**
 * @author ettear
 * Created by ettear on 16/10/2017.
 */
public class ActivitiElementVariable extends InputDataItem {
    public final static QName type = new QName(ActivitiBase.NAME_SPACE, "elementVariable");

}
