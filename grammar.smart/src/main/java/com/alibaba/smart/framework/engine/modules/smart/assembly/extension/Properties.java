package com.alibaba.smart.framework.engine.modules.smart.assembly.extension;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.model.assembly.Extension;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionContainer;
import com.alibaba.smart.framework.engine.modules.smart.assembly.SmartBase;

import lombok.Data;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
@Data
public class Properties implements Extension {
    public final static QName type = new QName(SmartBase.SMART_NS, "properties");

    private List<Value> extensionList  = new ArrayList();

    @Override
    public void decorate(ExtensionContainer extensionContainer) {
        for (Value value : extensionList) {
            extensionContainer.getProperties().put(value.getName(),value.getValue());
        }
    }

    //@Override
    //public boolean isPrepare() {
    //    return false;
    //}
}
