package com.alibaba.smart.framework.engine.modules.smart.assembly.extension;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;

import lombok.Data;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@Data
public class Value  implements Extension {
    public final static QName type = new QName(SmartBase.SMART_NS, "value");

    private String name;
    private String value;

    @Override
    public String getType() {
        return "Value";
    }

    @Override
    public Object decorate(ExtensionElements extensionElements) {
        //extensionElements.getProperties().put(name,value);
        return "";
        //FIMXE
    }
}
