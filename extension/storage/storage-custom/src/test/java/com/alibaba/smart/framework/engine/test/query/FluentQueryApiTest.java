package com.alibaba.smart.framework.engine.test.query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.query.TaskQuery;
import com.alibaba.smart.framework.engine.query.ProcessInstanceQuery;
import com.alibaba.smart.framework.engine.query.SupervisionQuery;
import com.alibaba.smart.framework.engine.query.NotificationQuery;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration tests for Fluent Query API.
 *
 * @author SmartEngine Team
 */
public class FluentQueryApiTest {

    protected ProcessEngineConfiguration processEngineConfiguration;
    protected SmartEngine smartEngine;
    protected RepositoryCommandService repositoryCommandService;
    protected ProcessCommandService processCommandService;
    protected TaskCommandService taskCommandService;

    @Before
    public void setUp() {
        PersisterSession.create();

        processEngineConfiguration = new DefaultProcessEngineConfiguration();
        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        repositoryCommandService = smartEngine.getRepositoryCommandService();
        processCommandService = smartEngine.getProcessCommandService();
        taskCommandService = smartEngine.getTaskCommandService();
    }

    @After
    public void tearDown() {
        PersisterSession.destroySession();
    }

    @Test
    public void testCreateTaskQuery() {
        // Deploy a simple process
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("basic-process.bpmn.xml").getFirstProcessDefinition();

        // Start process instance
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("route", "a");
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), variables);

        PersisterSession.currentSession().putProcessInstance(processInstance);

        // Test fluent query API
        TaskQuery query = smartEngine.createTaskQuery();
        Assert.assertNotNull(query);

        // Query with process instance ID - returns empty list since basic-process uses receiveTask not userTask
        List<TaskInstance> tasks = query
            .processInstanceId(processInstance.getInstanceId())
            .list();

        Assert.assertNotNull(tasks);
    }

    @Test
    public void testTaskQueryChaining() {
        TaskQuery query = smartEngine.createTaskQuery();

        // Test method chaining returns same query object
        TaskQuery chainedQuery = query
            .processInstanceId("123")
            .taskAssignee("user001")
            .taskStatus(TaskInstanceConstant.PENDING)
            .taskPriority(5)
            .pageOffset(0)
            .pageSize(10)
            .tenantId("tenant001")
            .orderByCreateTime()
            .desc();

        Assert.assertSame(query, chainedQuery);
    }

    @Test
    public void testProcessInstanceQueryChaining() {
        ProcessInstanceQuery query = smartEngine.createProcessQuery();

        // Test method chaining returns same query object
        ProcessInstanceQuery chainedQuery = query
            .processInstanceId("123")
            .startedBy("user001")
            .processStatus("running")
            .processDefinitionType("approval")
            .bizUniqueId("biz001")
            .pageOffset(0)
            .pageSize(20)
            .tenantId("tenant001")
            .orderByStartTime()
            .desc();

        Assert.assertSame(query, chainedQuery);
    }

    @Test
    public void testTaskQueryWithOrdering() {
        TaskQuery query = smartEngine.createTaskQuery();

        // Test multiple order by
        query
            .orderByPriority().desc()
            .orderByCreateTime().asc();

        // Should not throw exception
        Assert.assertNotNull(query);
    }

    @Test
    public void testProcessInstanceQueryCount() {
        // Deploy a simple process
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("basic-process.bpmn.xml").getFirstProcessDefinition();

        // Start process instance
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("route", "a");
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), variables);

        PersisterSession.currentSession().putProcessInstance(processInstance);

        // Count should work without errors
        long count = smartEngine.createProcessQuery()
            .processInstanceId(processInstance.getInstanceId())
            .count();

        Assert.assertTrue(count >= 0);
    }

    @Test
    public void testTaskQueryListPage() {
        TaskQuery query = smartEngine.createTaskQuery();

        // Test listPage method
        List<TaskInstance> tasks = query
            .taskStatus(TaskInstanceConstant.PENDING)
            .listPage(0, 10);

        Assert.assertNotNull(tasks);
    }

    @Test
    public void testProcessInstanceQuerySingleResult() {
        // Deploy a simple process
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("basic-process.bpmn.xml").getFirstProcessDefinition();

        // Start process instance
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("route", "a");
        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), variables);

        PersisterSession.currentSession().putProcessInstance(processInstance);

        // Query single result
        ProcessInstance result = smartEngine.createProcessQuery()
            .processInstanceId(processInstance.getInstanceId())
            .singleResult();

        Assert.assertNotNull(result);
        Assert.assertEquals(processInstance.getInstanceId(), result.getInstanceId());
    }

    @Test
    public void testSupervisionQueryChaining() {
        // Test SupervisionQuery chaining
        SupervisionQuery query = smartEngine.createSupervisionQuery()
            .supervisorUserId("supervisor001")
            .supervisionStatus("active")
            .orderByCreateTime().desc()
            .pageSize(10);

        Assert.assertNotNull(query);
    }

    @Test
    public void testNotificationQueryChaining() {
        // Test NotificationQuery chaining
        NotificationQuery query = smartEngine.createNotificationQuery()
            .receiverUserId("user001")
            .readStatus("unread")
            .orderByCreateTime().desc()
            .pageSize(20);

        Assert.assertNotNull(query);
    }
}
