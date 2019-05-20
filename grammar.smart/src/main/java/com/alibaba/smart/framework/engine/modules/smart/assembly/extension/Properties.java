package com.alibaba.smart.framework.engine.modules.smart.assembly.extension;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractBaseElement;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Properties extends AbstractBaseElement implements Extension {
    public final static QName type = new QName(SmartBase.SMART_NS, "properties");

    private List<Value> extensionList  = new ArrayList();

    @Override
    public boolean isPrepare() {
        return false;
    }
}
