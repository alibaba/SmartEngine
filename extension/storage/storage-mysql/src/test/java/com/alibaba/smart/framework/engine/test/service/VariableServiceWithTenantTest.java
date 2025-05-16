package com.alibaba.smart.framework.engine.test.service;

import com.alibaba.smart.framework.engine.instance.impl.DefaultVariableInstance;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.model.instance.VariableInstance;
import com.alibaba.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.alibaba.smart.framework.engine.test.process.helper.CustomVariablePersister;
import com.alibaba.smart.framework.engine.test.process.helper.DefaultMultiInstanceCounter;
import com.alibaba.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
import com.alibaba.smart.framework.engine.test.process.helper.dispatcher.IdAndGroupTaskAssigneeDispatcher;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class VariableServiceWithTenantTest extends DatabaseBaseTestCase {

    @Override
    protected void initProcessConfiguration() {
        super.initProcessConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new IdAndGroupTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
    }


    @Test
    public void test() throws Exception {
        String tenantId = null;
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("user-task-id-and-group-test.bpmn20.xml",null).getFirstProcessDefinition();


        //4.启动流程实例
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(),tenantId
        );
        Assert.assertNotNull(processInstance);

        String processInstanceInstanceId = processInstance.getInstanceId();

        String assumeExecutionInstanceId = "23333";


        PendingTaskQueryParam pendingTaskQueryParam = new PendingTaskQueryParam();
        pendingTaskQueryParam.setAssigneeUserId("testuser1");
        pendingTaskQueryParam.setAssigneeGroupIdList(Arrays.asList("testgroup11"));
        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());


        VariableInstance x = new DefaultVariableInstance();
        x.setInstanceId("1");
        x.setFieldKey("key");
        x.setFieldType(String.class);
        x.setFieldValue("value");
        x.setProcessInstanceId(processInstanceInstanceId);
        x.setExecutionInstanceId(assumeExecutionInstanceId);

        variableCommandService.insert(x);

        List<VariableInstance> variableList = variableQueryService.findList(
            processInstanceInstanceId,assumeExecutionInstanceId,null);

        Assert.assertEquals(1,variableList.size());

        VariableInstance variableInstance = variableList.get(0);
        Assert.assertEquals("key",variableInstance.getFieldKey());
        Assert.assertEquals(String.class,variableInstance.getFieldType());
        Assert.assertEquals("value",variableInstance.getFieldValue());

        Assert.assertEquals(processInstanceInstanceId,variableInstance.getProcessInstanceId());
        Assert.assertEquals(assumeExecutionInstanceId,variableInstance.getExecutionInstanceId());


    }


    @Test
    public void testWithTenantId() throws Exception {
        String tenantId = "-1";
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("user-task-id-and-group-test.bpmn20.xml",tenantId).getFirstProcessDefinition();


        //4.启动流程实例
        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),tenantId
        );
        Assert.assertNotNull(processInstance);

        String processInstanceInstanceId = processInstance.getInstanceId();

        String assumeExecutionInstanceId = "23333";


        PendingTaskQueryParam pendingTaskQueryParam = new PendingTaskQueryParam();
        pendingTaskQueryParam.setAssigneeUserId("testuser1");
        pendingTaskQueryParam.setAssigneeGroupIdList(Arrays.asList("testgroup11"));
        pendingTaskQueryParam.setTenantId(tenantId);
        List<TaskInstance> submitTaskInstanceList=  taskQueryService.findPendingTaskList(pendingTaskQueryParam);
        Assert.assertEquals(1,submitTaskInstanceList.size());


        VariableInstance x = new DefaultVariableInstance();
        x.setInstanceId("1");
        x.setFieldKey("key");
        x.setFieldType(String.class);
        x.setFieldValue("value");
        x.setProcessInstanceId(processInstanceInstanceId);
        x.setExecutionInstanceId(assumeExecutionInstanceId);
        x.setTenantId(tenantId);

        variableCommandService.insert(x);

        List<VariableInstance> variableList = variableQueryService.findList(
                processInstanceInstanceId,assumeExecutionInstanceId,tenantId);

        Assert.assertEquals(1,variableList.size());

        VariableInstance variableInstance = variableList.get(0);
        Assert.assertEquals("key",variableInstance.getFieldKey());
        Assert.assertEquals(String.class,variableInstance.getFieldType());
        Assert.assertEquals("value",variableInstance.getFieldValue());

        Assert.assertEquals(processInstanceInstanceId,variableInstance.getProcessInstanceId());
        Assert.assertEquals(assumeExecutionInstanceId,variableInstance.getExecutionInstanceId());


    }




}