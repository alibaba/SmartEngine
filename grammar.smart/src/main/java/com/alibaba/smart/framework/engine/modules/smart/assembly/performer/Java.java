package com.alibaba.smart.framework.engine.modules.smart.assembly.performer;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@Data
public class Java implements NoneIdBasedElement {
    public final static QName type = new QName(SmartBase.SMART_NS, "java");
    public final static QName classQName = new QName(SmartBase.SMART_NS, "class");

    private String className;

}
