package com.alibaba.smart.framework.engine.modules.base.assembly;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.assembly.impl.AbstractActivity;
import com.alibaba.smart.framework.engine.modules.common.assembly.SmartBase;

/**
 * Smart Activity Created by ettear on 16-4-14.
 */
public class SmartActivity extends AbstractActivity {

    public final static QName type = new QName(SmartBase.SMART_NS, "activity");
}
