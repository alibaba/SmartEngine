package com.alibaba.smart.framework.engine.smart;

import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.constant.SmartBase;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;

import lombok.Data;


@Data
public class Property implements PropertiesElementMarker, NoneIdBasedElement {
    public final static QName qtype = new QName(SmartBase.SMART_NS, "property");

    private String name;
    private String value;
    private String type;


    @Override
    public String getDecoratorType() {
        return ExtensionElementsConstant.PROPERTIES;
    }


}
