package com.alibaba.smart.framework.engine.test.service;

import java.util.Collection;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.CustomVariablePersister;
import com.alibaba.smart.framework.engine.test.process.DefaultMultiInstanceCounter;
import com.alibaba.smart.framework.engine.test.process.DoNothingLockStrategy;
import com.alibaba.smart.framework.engine.test.process.task.dispatcher.DefaultTaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.util.IOUtil;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RepositoryServiceTest extends DatabaseBaseTestCase {

    @Override
    protected void initProcessConfiguration() {
        super.initProcessConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
    }



    @Test
    public void testSimple() throws Exception {

        String content = IOUtil.readResourceFileAsUTF8String("multi-instance-test.bpmn20.xml");
        repositoryCommandService.deployWithUTF8Content(content);


        repositoryCommandService.deploy("test-usertask-and-servicetask-exclusive.bpmn20.xml");

        ProcessDefinition processDefinition = repositoryQueryService.getCachedProcessDefinition("exclusiveTest:1.0.0");

        Assert.assertNotNull(processDefinition);

        processDefinition = repositoryQueryService.getCachedProcessDefinition("exclusiveTest","1.0.0");

        Assert.assertNotNull(processDefinition);

        Collection<ProcessDefinition> processDefinitionCollection = repositoryQueryService.getAllCachedProcessDefinition();

        Assert.assertEquals(2,processDefinitionCollection.size());



    }

}