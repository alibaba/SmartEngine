package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.dialect.impl.H2Dialect;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.JsonCondition;
import com.alibaba.smart.framework.engine.service.param.query.JsonInCondition;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for domain_code and extra JSON fields on se_task_instance.
 */
public class TaskInstanceDomainCodeAndExtraTest extends BaseElementTest {

    @Setter(onMethod = @__({@Autowired}))
    TaskInstanceDAO taskDao;

    @Setter(onMethod = @__({@Autowired}))
    TaskAssigneeDAO taskAssigneeDAO;

    private final String tenantId = "-5";
    private final H2Dialect dialect = new H2Dialect();

    private static <T> List<T> listOf(T... items) {
        List<T> list = new ArrayList<T>();
        for (T item : items) {
            list.add(item);
        }
        return list;
    }

    private TaskInstanceEntity createTask(Long id, String domainCode, String extra) {
        TaskInstanceEntity task = new TaskInstanceEntity();
        task.setId(id);
        task.setGmtCreate(DateUtil.getCurrentDate());
        task.setGmtModified(DateUtil.getCurrentDate());
        task.setProcessDefinitionIdAndVersion("test:1.0");
        task.setActivityInstanceId(100L);
        task.setClaimUserId("user1");
        task.setExecutionInstanceId(200L);
        task.setPriority(500);
        task.setStatus(TaskInstanceConstant.PENDING);
        task.setProcessDefinitionActivityId("userTask1");
        task.setProcessInstanceId(300L);
        task.setProcessDefinitionType("approval");
        task.setTag("tag1");
        task.setTenantId(tenantId);
        task.setDomainCode(domainCode);
        task.setExtra(extra);
        return task;
    }

    @Before
    public void before() {
        TaskInstanceEntity t1 = createTask(5001L, "HR", "{\"category\":\"leave\",\"priority_level\":\"high\",\"department\":\"研发一部\"}");
        TaskInstanceEntity t2 = createTask(5002L, "Finance", "{\"category\":\"purchase\",\"priority_level\":\"low\",\"department\":\"财务部\"}");
        TaskInstanceEntity t3 = createTask(5003L, "HR", "{\"category\":\"purchase\",\"priority_level\":\"critical\",\"department\":\"研发二部\"}");
        TaskInstanceEntity t4 = createTask(5004L, null, null);

        taskDao.insert(t1);
        taskDao.insert(t2);
        taskDao.insert(t3);
        taskDao.insert(t4);
    }

    // ============ domain_code tests ============

    @Test
    public void testDomainCodeExactMatch() {
        TaskInstanceQueryParam param = new TaskInstanceQueryParam();
        param.setTenantId(tenantId);
        param.setDomainCode("HR");

        List<TaskInstanceEntity> result = taskDao.findTaskList(param);
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testDomainCodeIn() {
        TaskInstanceQueryParam param = new TaskInstanceQueryParam();
        param.setTenantId(tenantId);
        param.setDomainCodeList(listOf("HR", "Finance"));

        List<TaskInstanceEntity> result = taskDao.findTaskList(param);
        Assert.assertEquals(3, result.size());
    }

    @Test
    public void testDomainCodeLike() {
        TaskInstanceQueryParam param = new TaskInstanceQueryParam();
        param.setTenantId(tenantId);
        param.setDomainCodeLike("Fin");

        List<TaskInstanceEntity> result = taskDao.findTaskList(param);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("Finance", result.get(0).getDomainCode());
    }

    // ============ extra JSON tests ============

    @Test
    public void testJsonExactMatch() {
        TaskInstanceQueryParam param = new TaskInstanceQueryParam();
        param.setTenantId(tenantId);
        String expr = dialect.jsonExtractText("task.extra", "category");
        param.setJsonConditions(listOf(new JsonCondition(expr, "purchase")));

        List<TaskInstanceEntity> result = taskDao.findTaskList(param);
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testJsonInMatch() {
        TaskInstanceQueryParam param = new TaskInstanceQueryParam();
        param.setTenantId(tenantId);
        String expr = dialect.jsonExtractText("task.extra", "priority_level");
        param.setJsonInConditions(listOf(
                new JsonInCondition(expr, listOf("high", "critical"))));

        List<TaskInstanceEntity> result = taskDao.findTaskList(param);
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testJsonLikeMatch() {
        TaskInstanceQueryParam param = new TaskInstanceQueryParam();
        param.setTenantId(tenantId);
        String expr = dialect.jsonExtractText("task.extra", "department");
        param.setJsonLikeConditions(listOf(new JsonCondition(expr, "研发")));

        List<TaskInstanceEntity> result = taskDao.findTaskList(param);
        Assert.assertEquals(2, result.size());
    }

    @Test
    public void testJsonWithDomainCodeCombo() {
        TaskInstanceQueryParam param = new TaskInstanceQueryParam();
        param.setTenantId(tenantId);
        param.setDomainCode("HR");
        String expr = dialect.jsonExtractText("task.extra", "category");
        param.setJsonConditions(listOf(new JsonCondition(expr, "purchase")));

        List<TaskInstanceEntity> result = taskDao.findTaskList(param);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(Long.valueOf(5003L), result.get(0).getId());
    }

    @Test
    public void testNullExtra() {
        TaskInstanceQueryParam param = new TaskInstanceQueryParam();
        param.setTenantId(tenantId);
        String expr = dialect.jsonExtractText("task.extra", "category");
        param.setJsonConditions(listOf(new JsonCondition(expr, "leave")));

        List<TaskInstanceEntity> result = taskDao.findTaskList(param);
        // Only task 5001 has category=leave; task 5004 has null extra and should not match
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(Long.valueOf(5001L), result.get(0).getId());
    }

    @Test
    public void testInsertAndReadBackDomainCodeAndExtra() {
        TaskInstanceEntity result = taskDao.findOne(5001L, tenantId);
        Assert.assertNotNull(result);
        Assert.assertEquals("HR", result.getDomainCode());
        Assert.assertNotNull(result.getExtra());
        // PostgreSQL jsonb may reformat JSON, so check for key presence
        Assert.assertTrue(result.getExtra().contains("category"));
        Assert.assertTrue(result.getExtra().contains("leave"));
    }

    @Test
    public void testUpdateDomainCodeAndExtra() {
        TaskInstanceEntity task = taskDao.findOne(5001L, tenantId);
        Assert.assertEquals("HR", task.getDomainCode());

        task.setDomainCode("IT");
        task.setExtra("{\"category\":\"infra\"}");
        taskDao.update(task);

        TaskInstanceEntity updated = taskDao.findOne(5001L, tenantId);
        Assert.assertEquals("IT", updated.getDomainCode());
        Assert.assertTrue(updated.getExtra().contains("infra"));
    }

    @Test
    public void testJsonWithAssigneeQuery() {
        // Set up assignee for task 5001
        TaskAssigneeEntity assignee = new TaskAssigneeEntity();
        assignee.setId(9001L);
        assignee.setGmtCreate(DateUtil.getCurrentDate());
        assignee.setGmtModified(DateUtil.getCurrentDate());
        assignee.setAssigneeId("candidate1");
        assignee.setAssigneeType("user");
        assignee.setProcessInstanceId(300L);
        assignee.setTaskInstanceId(5001L);
        assignee.setTenantId(tenantId);
        taskAssigneeDAO.insert(assignee);

        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(tenantId);
        param.setAssigneeUserId("candidate1");
        param.setDomainCode("HR");

        List<TaskInstanceEntity> result = taskDao.findTaskByAssignee(param);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("HR", result.get(0).getDomainCode());
    }

    @Test
    public void testLargeJsonPayload() {
        StringBuilder sb = new StringBuilder("{\"data\":\"");
        // Build a >10KB JSON value
        for (int i = 0; i < 11000; i++) {
            sb.append("x");
        }
        sb.append("\"}");
        String largeJson = sb.toString();

        TaskInstanceEntity task = createTask(5010L, "LARGE", largeJson);
        taskDao.insert(task);

        TaskInstanceEntity result = taskDao.findOne(5010L, tenantId);
        Assert.assertNotNull(result);
        Assert.assertEquals("LARGE", result.getDomainCode());
        Assert.assertTrue(result.getExtra().length() > 10000);
    }
}
