package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.persister.database.entity.UserTaskIndexEntity;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DAO tests for se_user_task_index table.
 */
public class UserTaskIndexDAOTest extends BaseElementTest {

    @Setter(onMethod = @__({@Autowired}))
    UserTaskIndexDAO userTaskIndexDAO;

    UserTaskIndexEntity indexEntity = null;
    String tenantId = "sharding-test";

    @Before
    public void before() {
        indexEntity = new UserTaskIndexEntity();
        indexEntity.setTenantId(tenantId);
        indexEntity.setAssigneeId("user001");
        indexEntity.setAssigneeType("user");
        indexEntity.setTaskInstanceId(1001L);
        indexEntity.setProcessInstanceId(2001L);
        indexEntity.setProcessDefinitionType("approval");
        indexEntity.setDomainCode("HR");
        indexEntity.setTaskStatus("pending");
        indexEntity.setTaskGmtModified(DateUtil.getCurrentDate());
        indexEntity.setTitle("Test Task");
        indexEntity.setPriority(500);
        indexEntity.setGmtCreate(DateUtil.getCurrentDate());
        indexEntity.setGmtModified(DateUtil.getCurrentDate());
    }

    @Test
    public void testInsert() {
        userTaskIndexDAO.insert(indexEntity);
        Assert.assertNotNull(indexEntity.getId());
        Assert.assertTrue(indexEntity.getId() > 0);
    }

    @Test
    public void testFindByAssignee() {
        userTaskIndexDAO.insert(indexEntity);

        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(tenantId);
        param.setAssigneeUserId("user001");

        List<UserTaskIndexEntity> results = userTaskIndexDAO.findByAssignee(param);
        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("user001", results.get(0).getAssigneeId());
        Assert.assertEquals("pending", results.get(0).getTaskStatus());
        Assert.assertEquals("Test Task", results.get(0).getTitle());
    }

    @Test
    public void testCountByAssignee() {
        userTaskIndexDAO.insert(indexEntity);

        // Insert a second task for same assignee
        UserTaskIndexEntity entity2 = new UserTaskIndexEntity();
        entity2.setTenantId(tenantId);
        entity2.setAssigneeId("user001");
        entity2.setAssigneeType("user");
        entity2.setTaskInstanceId(1002L);
        entity2.setProcessInstanceId(2002L);
        entity2.setTaskStatus("pending");
        entity2.setGmtCreate(DateUtil.getCurrentDate());
        entity2.setGmtModified(DateUtil.getCurrentDate());
        userTaskIndexDAO.insert(entity2);

        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(tenantId);
        param.setAssigneeUserId("user001");

        Integer count = userTaskIndexDAO.countByAssignee(param);
        Assert.assertEquals(Integer.valueOf(2), count);
    }

    @Test
    public void testFindByAssigneeWithStatusFilter() {
        userTaskIndexDAO.insert(indexEntity);

        // Insert a completed task
        UserTaskIndexEntity completedEntity = new UserTaskIndexEntity();
        completedEntity.setTenantId(tenantId);
        completedEntity.setAssigneeId("user001");
        completedEntity.setAssigneeType("user");
        completedEntity.setTaskInstanceId(1003L);
        completedEntity.setProcessInstanceId(2003L);
        completedEntity.setTaskStatus("completed");
        completedEntity.setGmtCreate(DateUtil.getCurrentDate());
        completedEntity.setGmtModified(DateUtil.getCurrentDate());
        userTaskIndexDAO.insert(completedEntity);

        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(tenantId);
        param.setAssigneeUserId("user001");
        param.setStatus("pending");

        List<UserTaskIndexEntity> results = userTaskIndexDAO.findByAssignee(param);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("pending", results.get(0).getTaskStatus());
    }

    @Test
    public void testFindByAssigneeWithProcessDefinitionType() {
        userTaskIndexDAO.insert(indexEntity);

        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(tenantId);
        param.setAssigneeUserId("user001");
        param.setProcessDefinitionType("approval");

        List<UserTaskIndexEntity> results = userTaskIndexDAO.findByAssignee(param);
        Assert.assertEquals(1, results.size());

        // Non-matching type
        param.setProcessDefinitionType("non-existing-type");
        results = userTaskIndexDAO.findByAssignee(param);
        Assert.assertEquals(0, results.size());
    }

    @Test
    public void testDeleteByTaskInstanceId() {
        userTaskIndexDAO.insert(indexEntity);

        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(tenantId);
        param.setAssigneeUserId("user001");
        Assert.assertEquals(Integer.valueOf(1), userTaskIndexDAO.countByAssignee(param));

        userTaskIndexDAO.deleteByTaskInstanceId(1001L, tenantId);

        Assert.assertEquals(Integer.valueOf(0), userTaskIndexDAO.countByAssignee(param));
    }

    @Test
    public void testDeleteByAssigneeAndTask() {
        userTaskIndexDAO.insert(indexEntity);

        // Insert another assignee for same task
        UserTaskIndexEntity entity2 = new UserTaskIndexEntity();
        entity2.setTenantId(tenantId);
        entity2.setAssigneeId("user002");
        entity2.setAssigneeType("user");
        entity2.setTaskInstanceId(1001L);
        entity2.setProcessInstanceId(2001L);
        entity2.setTaskStatus("pending");
        entity2.setGmtCreate(DateUtil.getCurrentDate());
        entity2.setGmtModified(DateUtil.getCurrentDate());
        userTaskIndexDAO.insert(entity2);

        // Delete only user001's assignment
        userTaskIndexDAO.deleteByAssigneeAndTask("user001", 1001L, tenantId);

        // user002 should still have the task
        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(tenantId);
        param.setAssigneeUserId("user002");
        Assert.assertEquals(Integer.valueOf(1), userTaskIndexDAO.countByAssignee(param));

        // user001 should not
        param.setAssigneeUserId("user001");
        Assert.assertEquals(Integer.valueOf(0), userTaskIndexDAO.countByAssignee(param));
    }

    @Test
    public void testUpdateTaskStatus() {
        userTaskIndexDAO.insert(indexEntity);

        userTaskIndexDAO.updateTaskStatus(1001L, "completed", tenantId);

        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(tenantId);
        param.setAssigneeUserId("user001");

        List<UserTaskIndexEntity> results = userTaskIndexDAO.findByAssignee(param);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("completed", results.get(0).getTaskStatus());
    }

    @Test
    public void testUpdateTaskFields() {
        userTaskIndexDAO.insert(indexEntity);

        userTaskIndexDAO.updateTaskFields(1001L, "Updated Title", 999, "FINANCE", tenantId);

        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(tenantId);
        param.setAssigneeUserId("user001");

        List<UserTaskIndexEntity> results = userTaskIndexDAO.findByAssignee(param);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Updated Title", results.get(0).getTitle());
        Assert.assertEquals(Integer.valueOf(999), results.get(0).getPriority());
        Assert.assertEquals("FINANCE", results.get(0).getDomainCode());
    }

    @Test
    public void testFindByAssigneeWithPagination() {
        // Insert 5 tasks
        for (int i = 0; i < 5; i++) {
            UserTaskIndexEntity entity = new UserTaskIndexEntity();
            entity.setTenantId(tenantId);
            entity.setAssigneeId("user001");
            entity.setAssigneeType("user");
            entity.setTaskInstanceId(2000L + i);
            entity.setProcessInstanceId(3000L + i);
            entity.setTaskStatus("pending");
            entity.setGmtCreate(DateUtil.getCurrentDate());
            entity.setGmtModified(DateUtil.getCurrentDate());
            userTaskIndexDAO.insert(entity);
        }

        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(tenantId);
        param.setAssigneeUserId("user001");
        param.setPageOffset(0);
        param.setPageSize(3);

        List<UserTaskIndexEntity> page1 = userTaskIndexDAO.findByAssignee(param);
        Assert.assertEquals(3, page1.size());

        param.setPageOffset(3);
        List<UserTaskIndexEntity> page2 = userTaskIndexDAO.findByAssignee(param);
        Assert.assertEquals(2, page2.size());
    }
}
