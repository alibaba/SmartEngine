package com.alibaba.smart.framework.engine.model.assembly.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;

import lombok.Data;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
public abstract class AbstractActivity  implements Activity {

    private static final long serialVersionUID = 3558917263151685441L;

    private String id;
    private String name;

    private ExtensionElements extensionElements;

    private boolean startActivity;

    private Map<String,String> properties;

    @Override
    public boolean isStartActivity() {
        return startActivity;
    }

    public void setStartActivity(boolean startActivity) {
        this.startActivity = startActivity;
    }


}
