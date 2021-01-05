package com.alibaba.smart.framework.engine.smart;

import java.util.Map;

import javax.xml.namespace.QName;

import com.alibaba.smart.framework.engine.common.util.MapUtil;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.constant.SmartBase;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
public class PropertyCompositeKey {

    private String type;
    private String name;

    public PropertyCompositeKey(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public PropertyCompositeKey( String name) {
        this.name = name;
    }
}
