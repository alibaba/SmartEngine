package com.alibaba.smart.framework.engine.runtime.impl;

import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import lombok.Data;

import java.util.Map;

/**
 * Created by ettear on 16-4-12.
 */
@Data
public class DefaultRuntimeProcess extends DefaultRuntimeActivity implements RuntimeProcess{
    private String id;

    private String name;

    private String version;

    private ClassLoader classLoader;

    private Map<String,RuntimeActivity> activities;
}
