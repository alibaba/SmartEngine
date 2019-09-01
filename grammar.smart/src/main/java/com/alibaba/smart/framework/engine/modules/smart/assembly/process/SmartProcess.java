package com.alibaba.smart.framework.engine.modules.smart.assembly.process;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractProcess;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class SmartProcess extends AbstractProcess {

    public final static QName type = new QName(SmartBase.SMART_NS, "process");

    private static final long serialVersionUID = -2660788294142169268L;

    @Override
    public String toString() {
        return super.getId();
    }
}
