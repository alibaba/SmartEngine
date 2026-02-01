package com.alibaba.smart.framework.engine.test.dao;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.AssigneeOperationRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.BaseElementTest;
import com.alibaba.smart.framework.engine.persister.database.entity.AssigneeOperationRecordEntity;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * AssigneeOperationRecordDAO unit test
 *
 * @author SmartEngine Team
 */
public class AssigneeOperationRecordDAOTest extends BaseElementTest {

    @Setter(onMethod = @__({@Autowired}))
    private AssigneeOperationRecordDAO assigneeOperationRecordDAO;

    private static final String TENANT_ID = "-3";
    private static final Long TASK_INSTANCE_ID = 22345L;

    private AssigneeOperationRecordEntity entity;

    @Before
    public void before() {
        entity = new AssigneeOperationRecordEntity();
        entity.setId(3001L);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setTaskInstanceId(TASK_INSTANCE_ID);
        entity.setOperationType("add_assignee");
        entity.setOperatorUserId("manager001");
        entity.setTargetUserId("user011");
        entity.setOperationReason("Need to add technical expert for review");
        entity.setTenantId(TENANT_ID);
    }

    @Test
    public void testInsertAndSelect() {
        assigneeOperationRecordDAO.insert(entity);
        Assert.assertNotNull(entity.getId());

        AssigneeOperationRecordEntity retrieved = assigneeOperationRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertNotNull("Retrieved entity should not be null", retrieved);
        Assert.assertEquals("Task instance ID should match", TASK_INSTANCE_ID, retrieved.getTaskInstanceId());
        Assert.assertEquals("Operation type should match", "add_assignee", retrieved.getOperationType());
        Assert.assertEquals("Operator user ID should match", "manager001", retrieved.getOperatorUserId());
        Assert.assertEquals("Target user ID should match", "user011", retrieved.getTargetUserId());
    }

    @Test
    public void testOperationTypeValidation() {
        // Test add_assignee operation
        assigneeOperationRecordDAO.insert(entity);

        AssigneeOperationRecordEntity retrieved = assigneeOperationRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertEquals("Operation type should be add_assignee", "add_assignee", retrieved.getOperationType());

        // Test remove_assignee operation
        AssigneeOperationRecordEntity removeEntity = new AssigneeOperationRecordEntity();
        removeEntity.setId(3002L);
        removeEntity.setGmtCreate(DateUtil.getCurrentDate());
        removeEntity.setGmtModified(DateUtil.getCurrentDate());
        removeEntity.setTaskInstanceId(TASK_INSTANCE_ID);
        removeEntity.setOperationType("remove_assignee");
        removeEntity.setOperatorUserId("manager005");
        removeEntity.setTargetUserId("user016");
        removeEntity.setOperationReason("Remove assignee test");
        removeEntity.setTenantId(TENANT_ID);
        assigneeOperationRecordDAO.insert(removeEntity);

        AssigneeOperationRecordEntity retrieved2 = assigneeOperationRecordDAO.select(removeEntity.getId(), TENANT_ID);
        Assert.assertEquals("Operation type should be remove_assignee", "remove_assignee", retrieved2.getOperationType());
    }

    @Test
    public void testDelete() {
        assigneeOperationRecordDAO.insert(entity);

        AssigneeOperationRecordEntity retrieved = assigneeOperationRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertNotNull("Record should exist before delete", retrieved);

        assigneeOperationRecordDAO.delete(entity.getId(), TENANT_ID);

        retrieved = assigneeOperationRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertNull("Record should be deleted", retrieved);
    }

    @Test
    public void testSelectByTaskInstanceId() {
        Long taskId = 22347L;

        // Insert add_assignee record 1
        AssigneeOperationRecordEntity addEntity1 = new AssigneeOperationRecordEntity();
        addEntity1.setId(3003L);
        addEntity1.setGmtCreate(DateUtil.getCurrentDate());
        addEntity1.setGmtModified(DateUtil.getCurrentDate());
        addEntity1.setTaskInstanceId(taskId);
        addEntity1.setOperationType("add_assignee");
        addEntity1.setOperatorUserId("manager003");
        addEntity1.setTargetUserId("user013");
        addEntity1.setOperationReason("Add reason 1");
        addEntity1.setTenantId(TENANT_ID);
        assigneeOperationRecordDAO.insert(addEntity1);

        // Insert add_assignee record 2
        AssigneeOperationRecordEntity addEntity2 = new AssigneeOperationRecordEntity();
        addEntity2.setId(3004L);
        addEntity2.setGmtCreate(DateUtil.getCurrentDate());
        addEntity2.setGmtModified(DateUtil.getCurrentDate());
        addEntity2.setTaskInstanceId(taskId);
        addEntity2.setOperationType("add_assignee");
        addEntity2.setOperatorUserId("manager003");
        addEntity2.setTargetUserId("user014");
        addEntity2.setOperationReason("Add reason 2");
        addEntity2.setTenantId(TENANT_ID);
        assigneeOperationRecordDAO.insert(addEntity2);

        // Insert remove_assignee record
        AssigneeOperationRecordEntity removeEntity = new AssigneeOperationRecordEntity();
        removeEntity.setId(3005L);
        removeEntity.setGmtCreate(DateUtil.getCurrentDate());
        removeEntity.setGmtModified(DateUtil.getCurrentDate());
        removeEntity.setTaskInstanceId(taskId);
        removeEntity.setOperationType("remove_assignee");
        removeEntity.setOperatorUserId("manager003");
        removeEntity.setTargetUserId("user013");
        removeEntity.setOperationReason("Remove reason");
        removeEntity.setTenantId(TENANT_ID);
        assigneeOperationRecordDAO.insert(removeEntity);

        // Query all operation records for task
        List<AssigneeOperationRecordEntity> records = assigneeOperationRecordDAO.selectByTaskInstanceId(taskId, TENANT_ID);
        Assert.assertNotNull("Records should not be null", records);
        Assert.assertEquals("Should have 3 operation records", 3, records.size());

        // Verify operation types
        long addCount = records.stream()
            .filter(r -> "add_assignee".equals(r.getOperationType()))
            .count();
        long removeCount = records.stream()
            .filter(r -> "remove_assignee".equals(r.getOperationType()))
            .count();
        Assert.assertEquals("Should have 2 add operations", 2, addCount);
        Assert.assertEquals("Should have 1 remove operation", 1, removeCount);
    }

    @Test
    public void testUpdate() {
        assigneeOperationRecordDAO.insert(entity);

        AssigneeOperationRecordEntity retrieved = assigneeOperationRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertEquals("Need to add technical expert for review", retrieved.getOperationReason());

        entity.setOperationReason("Updated reason: employee has left");
        assigneeOperationRecordDAO.update(entity);

        retrieved = assigneeOperationRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertEquals("Operation reason should be updated", "Updated reason: employee has left", retrieved.getOperationReason());
    }
}
