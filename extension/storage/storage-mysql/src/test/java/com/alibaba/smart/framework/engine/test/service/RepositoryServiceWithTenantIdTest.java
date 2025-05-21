package com.alibaba.smart.framework.engine.test.service;

import com.alibaba.smart.framework.engine.common.util.IdAndVersionUtil;
import com.alibaba.smart.framework.engine.configuration.ConfigurationOption;
import com.alibaba.smart.framework.engine.configuration.impl.option.ProcessDefinitionMultiTenantShareOption;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.helper.CustomVariablePersister;
import com.alibaba.smart.framework.engine.test.process.helper.DefaultMultiInstanceCounter;
import com.alibaba.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import com.alibaba.smart.framework.engine.test.process.helper.dispatcher.DefaultTaskAssigneeDispatcher;
import com.alibaba.smart.framework.engine.util.IOUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RepositoryServiceWithTenantIdTest extends DatabaseBaseTestCase {

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

        String tenantId = "-1";
        String processDefinitionId = "exclusiveTest";
        String version = "1.0.0";

        String content = IOUtil.readResourceFileAsUTF8String("multi-instance-test.bpmn20.xml");
        repositoryCommandService.deployWithUTF8Content(content,tenantId);


        repositoryCommandService.deploy("test-usertask-and-servicetask-exclusive.bpmn20.xml",tenantId);

        ProcessDefinition processDefinition = repositoryQueryService.getCachedProcessDefinition(IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId, version,tenantId));

        Assert.assertNotNull(processDefinition);

        processDefinition = repositoryQueryService.getCachedProcessDefinition("exclusiveTest","1.0.0",tenantId);

        Assert.assertNotNull(processDefinition);

        Collection<ProcessDefinition> processDefinitionCollection = repositoryQueryService.getAllCachedProcessDefinition();

        Assert.assertEquals(2,processDefinitionCollection.size());

        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());

    }

    @Test
    public void testSimple1() throws Exception {

        String processDefinitionId = "exclusiveTest";
        String version = "1.0.0";

        String content = IOUtil.readResourceFileAsUTF8String("multi-instance-test.bpmn20.xml");
        repositoryCommandService.deployWithUTF8Content(content);


        repositoryCommandService.deploy("test-usertask-and-servicetask-exclusive.bpmn20.xml");




        ProcessDefinition processDefinition = repositoryQueryService.getCachedProcessDefinition(IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId, version,null));

        Assert.assertNotNull(processDefinition);


        processDefinition = repositoryQueryService.getCachedProcessDefinition(IdAndVersionUtil.buildProcessDefinitionKey(processDefinitionId, version));

        Assert.assertNotNull(processDefinition);


        String tenantId = "-1";

        processDefinition = repositoryQueryService.getCachedProcessDefinition("exclusiveTest","1.0.0",tenantId);

        Assert.assertNotNull(processDefinition);

        Collection<ProcessDefinition> processDefinitionCollection = repositoryQueryService.getAllCachedProcessDefinition();

        Assert.assertEquals(2,processDefinitionCollection.size());

        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
        ConfigurationOption processDefinitionMultiTenantMode = processEngineConfiguration.getOptionContainer()
                .get(ProcessDefinitionMultiTenantShareOption.PROCESS_DEFINITION_MULTI_TENANT_SHARE_OPTION.getId());

        Assert.assertTrue(processDefinitionMultiTenantMode.isEnabled());

        processEngineConfiguration.getOptionContainer().put(new ProcessDefinitionMultiTenantShareOption(false));

        processDefinitionMultiTenantMode = processEngineConfiguration.getOptionContainer()
                .get(ProcessDefinitionMultiTenantShareOption.PROCESS_DEFINITION_MULTI_TENANT_SHARE_OPTION.getId());
        Assert.assertFalse(processDefinitionMultiTenantMode.isEnabled());

        ProcessDefinition processDefinition1 = repositoryQueryService.getCachedProcessDefinition(IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId, version,tenantId));

        Assert.assertNull(processDefinition1);

        processEngineConfiguration.getOptionContainer().put(new ProcessDefinitionMultiTenantShareOption(true));

        processDefinitionMultiTenantMode = processEngineConfiguration.getOptionContainer()
                .get(ProcessDefinitionMultiTenantShareOption.PROCESS_DEFINITION_MULTI_TENANT_SHARE_OPTION.getId());
        Assert.assertTrue(processDefinitionMultiTenantMode.isEnabled());


        //部署的共享的，根据fullKey获取不到
        ProcessDefinition processDefinition2 = repositoryQueryService.getCachedProcessDefinition(IdAndVersionUtil.buildProcessDefinitionUniqueKey(processDefinitionId, version,tenantId));

        Assert.assertNull(processDefinition2);

    }

}