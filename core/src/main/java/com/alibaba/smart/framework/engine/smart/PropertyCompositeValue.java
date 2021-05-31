package com.alibaba.smart.framework.engine.smart;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;


@Data
@AllArgsConstructor
public class PropertyCompositeValue {

    private String value;
    private Map<String,String> attrMap;

}
