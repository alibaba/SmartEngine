package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.query.ActivityQueryService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.RepositoryQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.test.process.task.dispatcher.DefaultTaskAssigneeDispatcher;

import org.junit.After;
import org.junit.Before;

public class DatabaseBaseTestCase {

    protected ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();

    protected SmartEngine smartEngine = new DefaultSmartEngine();

    //3. 部署流程定义
    protected RepositoryCommandService repositoryCommandService;
    //2.获得常用服务
    protected ProcessCommandService processCommandService;
    protected ProcessQueryService processQueryService;

    protected ExecutionQueryService executionQueryService;
    protected ExecutionCommandService executionCommandService;
    protected RepositoryQueryService repositoryQueryService ;
    protected  TaskCommandService  taskCommandService;
    protected ActivityQueryService   activityQueryService;
    protected TaskQueryService  taskQueryService;

    @Before
    public void setUp() {
        SimpleAnnotationScanner.clear();

        initProcessConfiguation();

        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        //3. 部署流程定义
        repositoryCommandService = smartEngine
            .getRepositoryCommandService();
          repositoryQueryService = smartEngine
            .getRepositoryQueryService();

        processCommandService = smartEngine.getProcessCommandService();
        processQueryService = smartEngine.getProcessQueryService();
        executionQueryService = smartEngine.getExecutionQueryService();
        executionCommandService = smartEngine.getExecutionCommandService();

          processCommandService = smartEngine.getProcessCommandService();
           taskCommandService = smartEngine.getTaskCommandService();
            activityQueryService = smartEngine.getActivityQueryService();
          taskQueryService = smartEngine.getTaskQueryService();



    }

    protected void initProcessConfiguation() {
        processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
    }

    @After
    public void clear() {
        SimpleAnnotationScanner.clear();

    }

}