package com.alibaba.smart.framework.engine.runtime.impl;

import com.alibaba.smart.framework.engine.assembly.Process;
import com.alibaba.smart.framework.engine.runtime.RuntimeActivity;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import lombok.Data;

import java.util.Map;

/**
 * DefaultRuntimeProcess
 * Created by ettear on 16-4-12.
 */
@Data
public class DefaultRuntimeProcess extends DefaultRuntimeActivity<Process> implements RuntimeProcess{

    private ClassLoader classLoader;

    private Map<String,RuntimeActivity> activities;


}
