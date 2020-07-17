package com.alibaba.smart.framework.engine.extendsion.parser;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultIdGenerator;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinitionSource;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zilong.jiangzl
 * @create 2020-07-17 2:29 下午
 */
public class TestSmartEngineExt {


    private SmartEngine smartEngine;

    private RepositoryCommandService repositoryCommandService;

    private ProcessCommandService processCommandService;

    private ExecutionQueryService executionQueryService;

    private ExecutionCommandService executionCommandService;

    @Before
    public void initEngine() {
        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setIdGenerator(new DefaultIdGenerator());

        processEngineConfiguration.setAnnotationScanner(
            new SimpleAnnotationScanner(
                SmartEngine.class.getPackage().getName(),
                TestSmartEngineExt.class.getPackage().getName()));
        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        //2.获得常用服务
        repositoryCommandService = smartEngine.getRepositoryCommandService();
        processCommandService = smartEngine.getProcessCommandService();
        executionQueryService = smartEngine.getExecutionQueryService();
        executionCommandService = smartEngine.getExecutionCommandService();
    }

    @Test
    public void testServiceTask() throws Exception {
        //1. 部署流程定义
        ProcessDefinitionSource processDefinition = repositoryCommandService
                .deploy("process-def/extend/extend.bpmn20.xml");

        //2.启动流程实例
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getFirstProcessDefinition().getId(),  processDefinition.getFirstProcessDefinition().getVersion()
        );
        Assert.assertNotNull(processInstance);
    }
}
