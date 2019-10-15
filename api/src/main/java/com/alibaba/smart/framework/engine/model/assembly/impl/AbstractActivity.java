package com.alibaba.smart.framework.engine.model.assembly.impl;

import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.Activity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractActivity extends AbstractElement implements Activity {

    private static final long serialVersionUID = 3558917263151685441L;

    private boolean startActivity;

    @Getter
    @Setter
    private Map<String,String> properties;


    @Override
    public boolean isStartActivity() {
        return startActivity;
    }

    public void setStartActivity(boolean startActivity) {
        this.startActivity = startActivity;
    }


}
