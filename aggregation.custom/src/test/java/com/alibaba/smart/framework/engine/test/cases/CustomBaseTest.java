package com.alibaba.smart.framework.engine.test.cases;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.test.AliPayIdGenerator;

import org.junit.Before;

/**
 * Created by 高海军 帝奇 74394 on 2018 December  15:07.
 */
public class CustomBaseTest {

    protected SmartEngine smartEngine;

    protected ProcessCommandService processCommandService;
    protected ProcessQueryService processQueryService;
    protected ExecutionQueryService executionQueryService;
    protected ExecutionCommandService executionCommandService;

    @Before
    public void before() {
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setIdGenerator(new AliPayIdGenerator());

        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        processCommandService = smartEngine.getProcessCommandService();
        processQueryService = smartEngine.getProcessQueryService();

        executionQueryService = smartEngine.getExecutionQueryService();
        executionCommandService = smartEngine.getExecutionCommandService();

    }

}