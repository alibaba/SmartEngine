package com.alibaba.smart.framework.engine.modules.bpmn;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;

/**
 * @author dongdong.zdd
 * @since 2017-04-18
 */
public class Workflow {


    private static final DefaultSmartEngine smartEngine = new DefaultSmartEngine();



    public void init() {
        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
        smartEngine.init(processEngineConfiguration);
    }

    public SmartEngine getEngine() {
        return smartEngine;
    }



}
