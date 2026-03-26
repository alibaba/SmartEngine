package com.alibaba.smart.framework.engine.test.query;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.model.instance.DeploymentInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.DeploymentInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.ProcessInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.DeploymentInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for P1+P2 fluent query API enhancements.
 * Tests TaskQuery, ProcessInstanceQuery, and DeploymentQuery new methods.
 */
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class FluentQueryP1P2IntegrationTest extends DatabaseBaseTestCase {

    @Setter(onMethod = @__({@Autowired}))
    private TaskInstanceDAO taskInstanceDAO;

    @Setter(onMethod = @__({@Autowired}))
    private ProcessInstanceDAO processInstanceDAO;

    @Setter(onMethod = @__({@Autowired}))
    private DeploymentInstanceDAO deploymentInstanceDAO;

    private static final String TENANT_ID = "-5";

    // ID counters
    private long taskIdCounter = 50001L;
    private long processIdCounter = 50101L;
    private long deploymentIdCounter = 50201L;

    // Pre-created test data
    private TaskInstanceEntity task1, task2, task3, task4;
    private ProcessInstanceEntity process1, process2, process3;
    private DeploymentInstanceEntity deployment1, deployment2, deployment3;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        createTestData();
    }

    private void createTestData() {
        // Task 1: assigned, priority=5, type=approval, title="Review Purchase Order"
        task1 = createTask(taskIdCounter++, "pending", "approval", "user001", "Review Purchase Order", 5);
        // Task 2: unassigned, priority=10, type=approval, title="Approve Budget Report"
        task2 = createTask(taskIdCounter++, "pending", "approval", null, "Approve Budget Report", 10);
        // Task 3: assigned to user002, priority=1, type=leave, title="Submit Timesheet"
        task3 = createTask(taskIdCounter++, "completed", "leave", "user002", "Submit Timesheet", 1);
        // Task 4: assigned to admin-user, priority=8, type=leave, title="Review Leave Application"
        task4 = createTask(taskIdCounter++, "pending", "leave", "admin-user", "Review Leave Application", 8);

        // Process instances
        process1 = createProcess(processIdCounter++, "running", "approval", "user001");
        process2 = createProcess(processIdCounter++, "completed", "leave", "user002");
        process3 = createProcess(processIdCounter++, "running", "leave", "user001");

        // Deployments
        deployment1 = createDeployment(deploymentIdCounter++, "approval", "APPROVAL_V1",
            "Purchase Approval Flow", "active", "normal");
        deployment2 = createDeployment(deploymentIdCounter++, "leave", "LEAVE_V1",
            "Leave Application Flow", "active", "normal");
        deployment3 = createDeployment(deploymentIdCounter++, "approval", "APPROVAL_V2",
            "Purchase Approval Flow V2", "disabled", "normal");
    }

    // ====================== TaskQuery: createdAfter / createdBefore ======================

    @Test
    public void testCreatedAfterBefore() {
        Date beforeCreate = addMinutes(new Date(), -5);
        Date afterCreate = addMinutes(new Date(), 5);

        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .createdAfter(beforeCreate)
            .createdBefore(afterCreate)
            .list();

        Assert.assertEquals("Should find all 4 tasks within time range", 4, result.size());
    }

    @Test
    public void testCreatedAfterFuture() {
        Date futureDate = addMinutes(new Date(), 60);

        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .createdAfter(futureDate)
            .list();

        Assert.assertTrue("No tasks created in the future", result.isEmpty());
    }

    // ====================== TaskQuery: taskTitleLike ======================

    @Test
    public void testTaskTitleLike() {
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .taskTitleLike("Review")
            .list();

        Assert.assertEquals("Should find 2 tasks with 'Review' in title", 2, result.size());
    }

    @Test
    public void testTaskTitleLikeNoMatch() {
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .taskTitleLike("nonexistent")
            .list();

        Assert.assertTrue("No tasks should match", result.isEmpty());
    }

    @Test
    public void testTaskTitleLikeConditional() {
        // condition=false, titleLike should be ignored
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .taskTitleLike(false, "nonexistent")
            .list();

        Assert.assertEquals("Conditional=false should not filter", 4, result.size());
    }

    // ====================== TaskQuery: taskUnassigned ======================

    @Test
    public void testTaskUnassigned() {
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .taskUnassigned()
            .list();

        Assert.assertEquals("Should find 1 unassigned task", 1, result.size());
        Assert.assertNull("Task should have no assignee", result.get(0).getClaimUserId());
    }

    @Test
    public void testTaskUnassignedWithStatus() {
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .taskUnassigned()
            .taskStatus("pending")
            .list();

        Assert.assertEquals("Should find 1 unassigned pending task", 1, result.size());
    }

    // ====================== TaskQuery: processDefinitionTypeIn ======================

    @Test
    public void testProcessDefinitionTypeIn() {
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .processDefinitionTypeIn(Arrays.asList("approval", "leave"))
            .list();

        Assert.assertEquals("Should find all 4 tasks", 4, result.size());
    }

    @Test
    public void testProcessDefinitionTypeInSingle() {
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .processDefinitionTypeIn("approval")
            .list();

        Assert.assertEquals("Should find 2 approval tasks", 2, result.size());
    }

    @Test
    public void testProcessDefinitionTypeInNoMatch() {
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .processDefinitionTypeIn("nonexistent")
            .list();

        Assert.assertTrue("No tasks should match", result.isEmpty());
    }

    // ====================== TaskQuery: taskAssigneeLike ======================

    @Test
    public void testTaskAssigneeLike() {
        // "user" matches user001, user002, and admin-user (contains "user")
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .taskAssigneeLike("user00")
            .list();

        Assert.assertEquals("Should find 2 tasks with 'user00' in assignee", 2, result.size());
    }

    @Test
    public void testTaskAssigneeLikeAdmin() {
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .taskAssigneeLike("admin")
            .list();

        Assert.assertEquals("Should find 1 task with 'admin' in assignee", 1, result.size());
    }

    // ====================== TaskQuery: taskMinPriority / taskMaxPriority ======================

    @Test
    public void testTaskMinPriority() {
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .taskMinPriority(5)
            .list();

        Assert.assertEquals("Should find 3 tasks with priority >= 5", 3, result.size());
    }

    @Test
    public void testTaskMaxPriority() {
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .taskMaxPriority(5)
            .list();

        Assert.assertEquals("Should find 2 tasks with priority <= 5", 2, result.size());
    }

    @Test
    public void testTaskPriorityRange() {
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .taskMinPriority(5)
            .taskMaxPriority(8)
            .list();

        Assert.assertEquals("Should find 2 tasks with priority 5-8", 2, result.size());
    }

    // ====================== TaskQuery: withoutTenantId ======================

    @Test
    public void testWithoutTenantId() {
        // Query without tenant filter should return tasks from all tenants
        List<TaskInstance> withTenant = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .processDefinitionType("approval")
            .list();

        List<TaskInstance> withoutTenant = smartEngine.createTaskQuery()
            .withoutTenantId()
            .processDefinitionType("approval")
            .taskTitleLike("Review Purchase")
            .list();

        Assert.assertEquals("With tenant should find 2", 2, withTenant.size());
        // Without tenant may find more (from other test data), but should at least find ours
        Assert.assertTrue("Without tenant should find at least 1", withoutTenant.size() >= 1);
    }

    // ====================== TaskQuery: count with new filters ======================

    @Test
    public void testCountWithTitleLike() {
        long count = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .taskTitleLike("Review")
            .count();

        Assert.assertEquals("Should count 2 tasks with 'Review' in title", 2L, count);
    }

    @Test
    public void testCountWithPriorityRange() {
        long count = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .taskMinPriority(5)
            .taskMaxPriority(8)
            .count();

        Assert.assertEquals("Should count 2 tasks in priority range 5-8", 2L, count);
    }

    // ====================== TaskQuery: combined filters ======================

    @Test
    public void testCombinedFilters() {
        List<TaskInstance> result = smartEngine.createTaskQuery()
            .tenantId(TENANT_ID)
            .processDefinitionTypeIn("leave")
            .taskStatus("pending")
            .taskMinPriority(5)
            .list();

        Assert.assertEquals("Should find 1 pending leave task with priority >= 5", 1, result.size());
        Assert.assertEquals("Should be the Review Leave Application task",
            "Review Leave Application", result.get(0).getTitle());
    }

    // ====================== ProcessInstanceQuery: processDefinitionTypeIn ======================

    @Test
    public void testProcessDefinitionTypeInOnProcess() {
        List<ProcessInstance> result = smartEngine.createProcessQuery()
            .tenantId(TENANT_ID)
            .processDefinitionTypeIn(Arrays.asList("approval", "leave"))
            .list();

        Assert.assertEquals("Should find all 3 processes", 3, result.size());
    }

    @Test
    public void testProcessDefinitionTypeInSingleOnProcess() {
        List<ProcessInstance> result = smartEngine.createProcessQuery()
            .tenantId(TENANT_ID)
            .processDefinitionTypeIn("approval")
            .list();

        Assert.assertEquals("Should find 1 approval process", 1, result.size());
    }

    // ====================== ProcessInstanceQuery: involvedUser ======================

    @Test
    public void testInvolvedUser() {
        List<ProcessInstance> result = smartEngine.createProcessQuery()
            .tenantId(TENANT_ID)
            .involvedUser("user001")
            .list();

        Assert.assertEquals("user001 started 2 processes", 2, result.size());
    }

    @Test
    public void testInvolvedUserNoMatch() {
        List<ProcessInstance> result = smartEngine.createProcessQuery()
            .tenantId(TENANT_ID)
            .involvedUser("nonexistent")
            .list();

        Assert.assertTrue("No processes for nonexistent user", result.isEmpty());
    }

    // ====================== ProcessInstanceQuery: withoutTenantId ======================

    @Test
    public void testProcessQueryWithoutTenantId() {
        List<ProcessInstance> withTenant = smartEngine.createProcessQuery()
            .tenantId(TENANT_ID)
            .processDefinitionType("approval")
            .list();

        List<ProcessInstance> withoutTenant = smartEngine.createProcessQuery()
            .withoutTenantId()
            .involvedUser("user001")
            .processDefinitionType("approval")
            .list();

        Assert.assertEquals("With tenant should find 1 approval", 1, withTenant.size());
        Assert.assertTrue("Without tenant should find at least 1", withoutTenant.size() >= 1);
    }

    // ====================== DeploymentQuery: basic filters ======================

    @Test
    public void testDeploymentQueryByType() {
        List<DeploymentInstance> result = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .processDefinitionType("approval")
            .list();

        Assert.assertEquals("Should find 2 approval deployments", 2, result.size());
    }

    @Test
    public void testDeploymentQueryByCode() {
        List<DeploymentInstance> result = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .processDefinitionCode("LEAVE_V1")
            .list();

        Assert.assertEquals("Should find 1 leave deployment", 1, result.size());
    }

    @Test
    public void testDeploymentQueryByNameLike() {
        List<DeploymentInstance> result = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .processDefinitionNameLike("Purchase")
            .list();

        Assert.assertEquals("Should find 2 deployments with 'Purchase' in name", 2, result.size());
    }

    @Test
    public void testDeploymentQueryByNameLikeConditional() {
        List<DeploymentInstance> result = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .processDefinitionNameLike(false, "nonexistent")
            .list();

        Assert.assertEquals("Conditional=false should not filter", 3, result.size());
    }

    @Test
    public void testDeploymentQueryByStatus() {
        List<DeploymentInstance> result = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .deploymentStatus("active")
            .list();

        Assert.assertEquals("Should find 2 active deployments", 2, result.size());
    }

    @Test
    public void testDeploymentQueryByStatusConditional() {
        List<DeploymentInstance> result = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .deploymentStatus(false, "disabled")
            .list();

        Assert.assertEquals("Conditional=false should not filter", 3, result.size());
    }

    // ====================== DeploymentQuery: count ======================

    @Test
    public void testDeploymentQueryCount() {
        long count = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .processDefinitionType("approval")
            .count();

        Assert.assertEquals("Should count 2 approval deployments", 2L, count);
    }

    // ====================== DeploymentQuery: pagination ======================

    @Test
    public void testDeploymentQueryPagination() {
        List<DeploymentInstance> page1 = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .listPage(0, 2);

        Assert.assertEquals("Page 1 should have 2 items", 2, page1.size());

        List<DeploymentInstance> page2 = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .listPage(2, 2);

        Assert.assertEquals("Page 2 should have 1 item", 1, page2.size());
    }

    // ====================== DeploymentQuery: ordering ======================

    @Test
    public void testDeploymentQueryOrderByCreateTime() {
        List<DeploymentInstance> result = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .orderByCreateTime().asc()
            .list();

        Assert.assertEquals("Should find 3 deployments", 3, result.size());
    }

    // ====================== DeploymentQuery: singleResult ======================

    @Test
    public void testDeploymentQuerySingleResult() {
        DeploymentInstance result = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .processDefinitionCode("LEAVE_V1")
            .singleResult();

        Assert.assertNotNull("Should find single deployment", result);
        Assert.assertEquals("Should be LEAVE_V1", "LEAVE_V1", result.getProcessDefinitionCode());
    }

    @Test
    public void testDeploymentQuerySingleResultNull() {
        DeploymentInstance result = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .processDefinitionCode("NONEXISTENT")
            .singleResult();

        Assert.assertNull("Should return null for no match", result);
    }

    // ====================== DeploymentQuery: combined filters ======================

    @Test
    public void testDeploymentQueryCombinedFilters() {
        List<DeploymentInstance> result = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .processDefinitionType("approval")
            .deploymentStatus("active")
            .processDefinitionNameLike("Purchase")
            .list();

        Assert.assertEquals("Should find 1 active approval deployment", 1, result.size());
        Assert.assertEquals("Should be APPROVAL_V1", "APPROVAL_V1",
            result.get(0).getProcessDefinitionCode());
    }

    // ====================== DeploymentQuery: withoutTenantId ======================

    @Test
    public void testDeploymentQueryWithoutTenantId() {
        List<DeploymentInstance> withTenant = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .processDefinitionCode("LEAVE_V1")
            .list();

        List<DeploymentInstance> withoutTenant = smartEngine.createDeploymentQuery()
            .withoutTenantId()
            .processDefinitionCode("LEAVE_V1")
            .list();

        Assert.assertEquals("With tenant should find 1", 1, withTenant.size());
        Assert.assertTrue("Without tenant should find at least 1", withoutTenant.size() >= 1);
    }

    // ====================== DeploymentQuery: deploymentInstanceId ======================

    @Test
    public void testDeploymentQueryById() {
        DeploymentInstance result = smartEngine.createDeploymentQuery()
            .tenantId(TENANT_ID)
            .deploymentInstanceId(String.valueOf(deployment1.getId()))
            .singleResult();

        Assert.assertNotNull("Should find deployment by ID", result);
        Assert.assertEquals("APPROVAL_V1", result.getProcessDefinitionCode());
    }

    // ====================== Helper Methods ======================

    private TaskInstanceEntity createTask(long id, String status, String processDefinitionType,
                                          String claimUserId, String title, int priority) {
        TaskInstanceEntity entity = new TaskInstanceEntity();
        entity.setId(id);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setProcessInstanceId(processIdCounter);
        entity.setExecutionInstanceId(id + 1000L);
        entity.setActivityInstanceId(id + 2000L);
        entity.setProcessDefinitionIdAndVersion("testProcess:1");
        entity.setProcessDefinitionActivityId("userTask1");
        entity.setProcessDefinitionType(processDefinitionType);
        entity.setClaimUserId(claimUserId);
        entity.setTitle(title);
        entity.setPriority(priority);
        entity.setStatus(status);
        entity.setTenantId(TENANT_ID);
        taskInstanceDAO.insert(entity);
        return entity;
    }

    private ProcessInstanceEntity createProcess(long id, String status, String processDefinitionType,
                                                 String startUserId) {
        ProcessInstanceEntity entity = new ProcessInstanceEntity();
        entity.setId(id);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setProcessDefinitionIdAndVersion("testProcess:1");
        entity.setProcessDefinitionType(processDefinitionType);
        entity.setStartUserId(startUserId);
        entity.setStatus(status);
        entity.setTenantId(TENANT_ID);
        processInstanceDAO.insert(entity);
        return entity;
    }

    private DeploymentInstanceEntity createDeployment(long id, String processDefinitionType,
                                                       String code, String name, String status,
                                                       String logicStatus) {
        DeploymentInstanceEntity entity = new DeploymentInstanceEntity();
        entity.setId(id);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setProcessDefinitionId("pd_" + id);
        entity.setProcessDefinitionVersion("1.0");
        entity.setProcessDefinitionType(processDefinitionType);
        entity.setProcessDefinitionCode(code);
        entity.setProcessDefinitionName(name);
        entity.setProcessDefinitionDesc("Test deployment " + name);
        entity.setProcessDefinitionContent("<xml/>");
        entity.setDeploymentUserId("deployer001");
        entity.setDeploymentStatus(status);
        entity.setLogicStatus(logicStatus);
        entity.setTenantId(TENANT_ID);
        deploymentInstanceDAO.insert(entity);
        return entity;
    }

    private Date addMinutes(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return cal.getTime();
    }
}
