package com.alibaba.smart.framework.engine.modules.smart.assembly.extension;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.Extension;
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
public class Value extends Properties implements Extension {
    public final static QName type = new QName(SmartBase.SMART_NS, "value");

    private String name;
    private String value;

    @Override
    public boolean isPrepare() {
        return true;
    }
}
