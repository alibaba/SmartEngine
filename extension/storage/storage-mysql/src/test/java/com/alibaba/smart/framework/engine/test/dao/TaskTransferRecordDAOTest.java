package com.alibaba.smart.framework.engine.test.dao;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.BaseElementTest;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskTransferRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskTransferRecordEntity;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * TaskTransferRecordDAO unit test
 *
 * @author SmartEngine Team
 */
public class TaskTransferRecordDAOTest extends BaseElementTest {

    @Setter(onMethod = @__({@Autowired}))
    private TaskTransferRecordDAO taskTransferRecordDAO;

    private static final String TENANT_ID = "-3";
    private static final Long TASK_INSTANCE_ID = 12345L;

    private TaskTransferRecordEntity entity;

    @Before
    public void before() {
        entity = new TaskTransferRecordEntity();
        entity.setId(5001L);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setTaskInstanceId(TASK_INSTANCE_ID);
        entity.setFromUserId("user001");
        entity.setToUserId("user002");
        entity.setTransferReason("Workload too heavy, need assistance");
        entity.setDeadline(new Date(System.currentTimeMillis() + 86400000)); // 1 day later
        entity.setTenantId(TENANT_ID);
    }

    @Test
    public void testInsertAndSelect() {
        taskTransferRecordDAO.insert(entity);
        Assert.assertNotNull(entity.getId());

        TaskTransferRecordEntity retrieved = taskTransferRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertNotNull("Retrieved entity should not be null", retrieved);
        Assert.assertEquals("Task instance ID should match", TASK_INSTANCE_ID, retrieved.getTaskInstanceId());
        Assert.assertEquals("From user ID should match", "user001", retrieved.getFromUserId());
        Assert.assertEquals("To user ID should match", "user002", retrieved.getToUserId());
        Assert.assertEquals("Transfer reason should match", "Workload too heavy, need assistance", retrieved.getTransferReason());
        Assert.assertNotNull("Deadline should not be null", retrieved.getDeadline());
    }

    @Test
    public void testDelete() {
        taskTransferRecordDAO.insert(entity);

        TaskTransferRecordEntity retrieved = taskTransferRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertNotNull("Record should exist before delete", retrieved);

        taskTransferRecordDAO.delete(entity.getId(), TENANT_ID);

        retrieved = taskTransferRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertNull("Record should be deleted", retrieved);
    }

    @Test
    public void testSelectByTaskInstanceId() {
        Long taskId = 12347L;

        // Insert 3 transfer records
        for (int i = 0; i < 3; i++) {
            TaskTransferRecordEntity transferEntity = new TaskTransferRecordEntity();
            transferEntity.setId(5002L + i);
            transferEntity.setGmtCreate(DateUtil.getCurrentDate());
            transferEntity.setGmtModified(DateUtil.getCurrentDate());
            transferEntity.setTaskInstanceId(taskId);
            transferEntity.setFromUserId("user00" + i);
            transferEntity.setToUserId("user00" + (i + 1));
            transferEntity.setTransferReason("Transfer reason " + i);
            transferEntity.setTenantId(TENANT_ID);
            taskTransferRecordDAO.insert(transferEntity);
        }

        // Query all transfer records for task
        List<TaskTransferRecordEntity> records = taskTransferRecordDAO.selectByTaskInstanceId(taskId, TENANT_ID);
        Assert.assertNotNull("Records should not be null", records);
        Assert.assertEquals("Should have 3 transfer records", 3, records.size());
    }

    @Test
    public void testUpdate() {
        taskTransferRecordDAO.insert(entity);

        TaskTransferRecordEntity retrieved = taskTransferRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertEquals("Workload too heavy, need assistance", retrieved.getTransferReason());

        entity.setTransferReason("Updated transfer reason");
        entity.setDeadline(new Date(System.currentTimeMillis() + 172800000)); // 2 days later
        taskTransferRecordDAO.update(entity);

        retrieved = taskTransferRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertEquals("Transfer reason should be updated", "Updated transfer reason", retrieved.getTransferReason());
        Assert.assertNotNull("Deadline should be set", retrieved.getDeadline());
    }

    @Test
    public void testTenantIsolation() {
        String tenant1 = "-3";
        String tenant2 = "-4";

        // Insert record for tenant1
        TaskTransferRecordEntity entity1 = new TaskTransferRecordEntity();
        entity1.setId(5010L);
        entity1.setGmtCreate(DateUtil.getCurrentDate());
        entity1.setGmtModified(DateUtil.getCurrentDate());
        entity1.setTaskInstanceId(12349L);
        entity1.setFromUserId("user007");
        entity1.setToUserId("user008");
        entity1.setTransferReason("Record for tenant1");
        entity1.setTenantId(tenant1);
        taskTransferRecordDAO.insert(entity1);

        // Insert record for tenant2
        TaskTransferRecordEntity entity2 = new TaskTransferRecordEntity();
        entity2.setId(5011L);
        entity2.setGmtCreate(DateUtil.getCurrentDate());
        entity2.setGmtModified(DateUtil.getCurrentDate());
        entity2.setTaskInstanceId(12349L);
        entity2.setFromUserId("user009");
        entity2.setToUserId("user010");
        entity2.setTransferReason("Record for tenant2");
        entity2.setTenantId(tenant2);
        taskTransferRecordDAO.insert(entity2);

        // Tenant1 should see its record
        TaskTransferRecordEntity retrieved1 = taskTransferRecordDAO.select(entity1.getId(), tenant1);
        Assert.assertNotNull("Tenant1 should see its record", retrieved1);

        // Tenant1 should not see tenant2's record
        TaskTransferRecordEntity retrieved2 = taskTransferRecordDAO.select(entity2.getId(), tenant1);
        Assert.assertNull("Tenant1 should not see tenant2's record", retrieved2);

        // Tenant2 should see its record
        TaskTransferRecordEntity retrieved3 = taskTransferRecordDAO.select(entity2.getId(), tenant2);
        Assert.assertNotNull("Tenant2 should see its record", retrieved3);
    }
}
