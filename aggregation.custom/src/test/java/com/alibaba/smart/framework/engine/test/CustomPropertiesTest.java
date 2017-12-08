package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.RepositoryQueryService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by 高海军 帝奇 74394 on 2017 November  17:54.
 */
public class CustomPropertiesTest {


    @Test
    public void test() throws Exception {

        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();


        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
            .getRepositoryCommandService();

        RepositoryQueryService repositoryQueryService = smartEngine
            .getRepositoryQueryService();


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("custom-properties.bpmn.xml");
        assertEquals(9, processDefinition.getProcess().getElements().size());


        processDefinition =  repositoryQueryService.getCachedProcessDefinition("custom-properties","1.0.0");
        assertEquals(9, processDefinition.getProcess().getElements().size());

        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        ProcessInstance processInstance = processCommandService.start(processDefinition.getId(), processDefinition.getVersion());
        processCommandService.abort(processInstance.getInstanceId());

    }

    @Before
    public void before(){
        PersisterSession.create();
    }

    @After
    public void after(){
        PersisterSession.destroySession();
    }



}