package com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.multi.instance;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.bpmn.assembly.multi.instance.LoopCollection;
import com.alibaba.smart.framework.engine.modules.compatible.activiti.assembly.ActivitiBase;

/**
 * @author ettear
 * Created by ettear on 15/10/2017.
 */
public class Collection extends LoopCollection {
    public final static QName type = new QName(ActivitiBase.NAME_SPACE, "collection");

}
