package com.alibaba.smart.framework.engine.modules.base.assembly;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractProcess;
import com.alibaba.smart.framework.engine.modules.common.assembly.SmartBase;

/**
 * Smart Process Created by ettear on 16-4-14.
 */
public class SmartProcess extends AbstractProcess {

    public final static QName type = new QName(SmartBase.SMART_NS, "process");

}
