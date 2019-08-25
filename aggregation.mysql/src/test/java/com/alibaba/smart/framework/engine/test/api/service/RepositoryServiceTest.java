package com.alibaba.smart.framework.engine.test.api.service;

import java.util.Collection;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.util.IOUtil;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.RepositoryQueryService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RepositoryServiceTest {


    @Test
    public void testSimple() throws Exception {

        //TODO 增加对本地内存的 tc。

        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);


        //2.获得常用服务
        RepositoryCommandService repositoryCommandService = smartEngine.getRepositoryCommandService();
        RepositoryQueryService repositoryQueryService =  smartEngine.getRepositoryQueryService();

        String content = IOUtil.readResourceFileAsUTF8String("multi-instance-test.bpmn20.xml");
        repositoryCommandService.deployWithUTF8Content(content);


        repositoryCommandService.deploy("test-usertask-and-servicetask-exclusive.bpmn20.xml");

        ProcessDefinition processDefinition = repositoryQueryService.getCachedProcessDefinition("test-multi-instance-user-task:1.0.1");

        Assert.assertNotNull(processDefinition);

        processDefinition = repositoryQueryService.getCachedProcessDefinition("test-multi-instance-user-task","1.0.1");

        Assert.assertNotNull(processDefinition);

        Collection<ProcessDefinition> processDefinitionCollection = repositoryQueryService.getAllCachedProcessDefinition();

        Assert.assertEquals(2,processDefinitionCollection.size());



    }

}