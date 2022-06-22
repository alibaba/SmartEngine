package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.InstanceAccessor;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.configuration.scanner.AnnotationScanner;
import com.alibaba.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.alibaba.smart.framework.engine.service.command.DeploymentCommandService;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.command.VariableCommandService;
import com.alibaba.smart.framework.engine.service.query.ActivityQueryService;
import com.alibaba.smart.framework.engine.service.query.DeploymentQueryService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.RepositoryQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskAssigneeQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.service.query.VariableQueryService;
import com.alibaba.smart.framework.engine.test.process.helper.dispatcher.DefaultTaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.test.process.helper.sequece.RandomIdGenerator;
import com.alibaba.smart.framework.engine.util.ClassUtil;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class DatabaseBaseTestCase implements ApplicationContextAware {

    private ApplicationContext applicationContext;

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

    protected DeploymentCommandService deploymentCommandService;
    protected DeploymentQueryService deploymentQueryService;

    protected VariableCommandService variableCommandService;
    protected VariableQueryService variableQueryService;
    protected TaskAssigneeQueryService taskAssigneeQueryService;

    private AnnotationScanner annotationScanner;

    @Before
    public void setUp() {

        this.annotationScanner = new SimpleAnnotationScanner(SmartEngine.class.getPackage().getName());

        initProcessConfiguration();

        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        //3. 部署流程定义
        deploymentCommandService = smartEngine.getDeploymentCommandService();

        repositoryCommandService = smartEngine
            .getRepositoryCommandService();
        processCommandService = smartEngine.getProcessCommandService();
        executionCommandService = smartEngine.getExecutionCommandService();
        taskCommandService = smartEngine.getTaskCommandService();

        deploymentQueryService =  smartEngine.getDeploymentQueryService();
        repositoryQueryService = smartEngine
            .getRepositoryQueryService();
        processQueryService = smartEngine.getProcessQueryService();
        executionQueryService = smartEngine.getExecutionQueryService();
        activityQueryService = smartEngine.getActivityQueryService();
        taskQueryService = smartEngine.getTaskQueryService();

        variableQueryService = smartEngine.getVariableQueryService();
        taskAssigneeQueryService = smartEngine.getTaskAssigneeQueryService();

        variableCommandService  = smartEngine.getVariableCommandService();


    }

    protected void initProcessConfiguration() {
        processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setIdGenerator(new RandomIdGenerator());
        processEngineConfiguration.setInstanceAccessor(new InnerInstanceAccessService());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
    }

    @After
    public void clear() {
        annotationScanner.clear();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private class InnerInstanceAccessService implements InstanceAccessor {
        @Override
        public Object access(String name) {

                Object bean = applicationContext.getBean(name);
                return bean;

        }

    }

}