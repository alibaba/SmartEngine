package com.alibaba.smart.framework.engine.smart;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class PropertyCompositeValue {

    private String value;
    private Map<String,String> attrMap;

}
