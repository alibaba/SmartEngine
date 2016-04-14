package com.alibaba.smart.framework.engine.modules.base.assembly;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractProcessDefinition;

import javax.xml.namespace.QName;

/**
 * Smart Process Definition
 * Created by ettear on 16-4-14.
 */
public class SmartProcessDefinition extends AbstractProcessDefinition {
    public final static QName type = new QName(SmartBase.SMART_NS, "definitions");
}
