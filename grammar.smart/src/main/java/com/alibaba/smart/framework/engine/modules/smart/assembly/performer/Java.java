package com.alibaba.smart.framework.engine.modules.smart.assembly.performer;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.Performable;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractBaseElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Java extends AbstractBaseElement implements Performable{
    public final static QName type = new QName(SmartBase.SMART_NS, "java");
    public final static QName classQName = new QName(SmartBase.SMART_NS, "class");

    private String action;
    private String className;

}
