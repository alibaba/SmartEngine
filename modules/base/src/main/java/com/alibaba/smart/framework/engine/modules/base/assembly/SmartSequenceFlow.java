package com.alibaba.smart.framework.engine.modules.base.assembly;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractTransition;
import com.alibaba.smart.framework.engine.modules.common.assembly.SmartBase;

/**
 * Smart Sequence Flow Created by ettear on 16-4-14.
 */
public class SmartSequenceFlow extends AbstractTransition {

    public final static QName type = new QName(SmartBase.SMART_NS, "sequenceFlow");
}
