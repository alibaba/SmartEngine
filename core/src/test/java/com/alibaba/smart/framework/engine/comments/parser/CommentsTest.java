package com.alibaba.smart.framework.engine.comments.parser;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultIdGenerator;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinitionSource;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author guoxing
 * @date 2020年11月24日14:42:55
 */
public class CommentsTest {

    private SmartEngine smartEngine;

    private RepositoryCommandService repositoryCommandService;

    private ProcessCommandService processCommandService;

    private ExecutionQueryService executionQueryService;

    private ExecutionCommandService executionCommandService;

    @Before
    public void initEngine() {
        // 1.初始化
        ProcessEngineConfiguration processEngineConfiguration =
                new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setIdGenerator(new DefaultIdGenerator());

        processEngineConfiguration.setAnnotationScanner(
                new SimpleAnnotationScanner(
                        SmartEngine.class.getPackage().getName(),
                        CommentsTest.class.getPackage().getName()));
        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        // 2.获得常用服务
        repositoryCommandService = smartEngine.getRepositoryCommandService();
        processCommandService = smartEngine.getProcessCommandService();
        executionQueryService = smartEngine.getExecutionQueryService();
        executionCommandService = smartEngine.getExecutionCommandService();
    }

    @Test
    public void testParseCommentElements() throws Exception {
        String tenantId = "-1";
        // 1. 部署流程定义
        ProcessDefinitionSource processDefinitionSource =
                repositoryCommandService.deploy(
                        "process-def/comments/comments.bpmn20.xml", tenantId);

        ProcessDefinition processDefinition =
                processDefinitionSource.getProcessDefinitionList().get(0);
        Assert.assertNotNull(processDefinition);
    }
}
