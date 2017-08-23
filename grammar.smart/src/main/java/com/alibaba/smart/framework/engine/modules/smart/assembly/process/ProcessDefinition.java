package com.alibaba.smart.framework.engine.modules.smart.assembly.process;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractProcessDefinition;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class ProcessDefinition extends AbstractProcessDefinition {

    public final static QName type = new QName(SmartBase.SMART_NS, "definitions");

    private static final long serialVersionUID = -7973338663278156625L;

}
