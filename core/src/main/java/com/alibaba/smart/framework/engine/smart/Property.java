package com.alibaba.smart.framework.engine.smart;

import java.util.Map;

import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.model.assembly.NoneIdBasedElement;

import lombok.Data;


@Data
public class Property implements PropertiesElementMarker, NoneIdBasedElement,CustomExtensionElement {

    public final static String xmlLocalPart = "property";

    private String name;
    private String value;
    private String type;

    private Map<String,String> attrs;

    @Override
    public String getDecoratorType() {
        return ExtensionElementsConstant.PROPERTIES;
    }


}
