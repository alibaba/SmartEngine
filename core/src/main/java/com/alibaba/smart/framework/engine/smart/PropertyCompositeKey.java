package com.alibaba.smart.framework.engine.smart;

import lombok.Data;

@Data
public class PropertyCompositeKey {

    private String type;
    private String name;

    public PropertyCompositeKey(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public PropertyCompositeKey(String name) {
        this.name = name;
    }
}
