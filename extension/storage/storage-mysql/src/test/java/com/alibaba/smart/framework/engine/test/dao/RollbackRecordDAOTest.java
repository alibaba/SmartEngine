package com.alibaba.smart.framework.engine.test.dao;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.BaseElementTest;
import com.alibaba.smart.framework.engine.persister.database.dao.RollbackRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.RollbackRecordEntity;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * RollbackRecordDAO unit test
 *
 * @author SmartEngine Team
 */
public class RollbackRecordDAOTest extends BaseElementTest {

    @Setter(onMethod = @__({@Autowired}))
    private RollbackRecordDAO rollbackRecordDAO;

    private static final String TENANT_ID = "-3";
    private static final Long PROCESS_INSTANCE_ID = 32345L;
    private static final Long TASK_INSTANCE_ID = 42345L;

    private RollbackRecordEntity entity;

    @Before
    public void before() {
        entity = new RollbackRecordEntity();
        entity.setId(4001L);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setProcessInstanceId(PROCESS_INSTANCE_ID);
        entity.setTaskInstanceId(TASK_INSTANCE_ID);
        entity.setRollbackType("specific");
        entity.setFromActivityId("userTask2");
        entity.setToActivityId("userTask1");
        entity.setOperatorUserId("operator001");
        entity.setRollbackReason("Found error in previous step, need to rollback");
        entity.setTenantId(TENANT_ID);
    }

    @Test
    public void testInsertAndSelect() {
        rollbackRecordDAO.insert(entity);
        Assert.assertNotNull(entity.getId());

        RollbackRecordEntity retrieved = rollbackRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertNotNull("Retrieved entity should not be null", retrieved);
        Assert.assertEquals("Process instance ID should match", PROCESS_INSTANCE_ID, retrieved.getProcessInstanceId());
        Assert.assertEquals("Task instance ID should match", TASK_INSTANCE_ID, retrieved.getTaskInstanceId());
        Assert.assertEquals("Rollback type should match", "specific", retrieved.getRollbackType());
        Assert.assertEquals("From activity ID should match", "userTask2", retrieved.getFromActivityId());
        Assert.assertEquals("To activity ID should match", "userTask1", retrieved.getToActivityId());
        Assert.assertEquals("Operator user ID should match", "operator001", retrieved.getOperatorUserId());
    }

    @Test
    public void testSelectByProcessInstanceId() {
        Long processId = 32347L;

        // Insert first rollback record
        RollbackRecordEntity entity1 = new RollbackRecordEntity();
        entity1.setId(4002L);
        entity1.setGmtCreate(DateUtil.getCurrentDate());
        entity1.setGmtModified(DateUtil.getCurrentDate());
        entity1.setProcessInstanceId(processId);
        entity1.setTaskInstanceId(42347L);
        entity1.setRollbackType("specific");
        entity1.setFromActivityId("userTask3");
        entity1.setToActivityId("userTask2");
        entity1.setOperatorUserId("operator003");
        entity1.setRollbackReason("First rollback");
        entity1.setTenantId(TENANT_ID);
        rollbackRecordDAO.insert(entity1);

        // Insert second rollback record
        RollbackRecordEntity entity2 = new RollbackRecordEntity();
        entity2.setId(4003L);
        entity2.setGmtCreate(DateUtil.getCurrentDate());
        entity2.setGmtModified(DateUtil.getCurrentDate());
        entity2.setProcessInstanceId(processId);
        entity2.setTaskInstanceId(42348L);
        entity2.setRollbackType("specific");
        entity2.setFromActivityId("userTask2");
        entity2.setToActivityId("userTask1");
        entity2.setOperatorUserId("operator003");
        entity2.setRollbackReason("Second rollback");
        entity2.setTenantId(TENANT_ID);
        rollbackRecordDAO.insert(entity2);

        // Insert third rollback record
        RollbackRecordEntity entity3 = new RollbackRecordEntity();
        entity3.setId(4004L);
        entity3.setGmtCreate(DateUtil.getCurrentDate());
        entity3.setGmtModified(DateUtil.getCurrentDate());
        entity3.setProcessInstanceId(processId);
        entity3.setTaskInstanceId(42349L);
        entity3.setRollbackType("specific");
        entity3.setFromActivityId("userTask4");
        entity3.setToActivityId("userTask3");
        entity3.setOperatorUserId("operator004");
        entity3.setRollbackReason("Third rollback");
        entity3.setTenantId(TENANT_ID);
        rollbackRecordDAO.insert(entity3);

        // Query all rollback records for process
        List<RollbackRecordEntity> records = rollbackRecordDAO.selectByProcessInstanceId(processId, TENANT_ID);
        Assert.assertNotNull("Records should not be null", records);
        Assert.assertEquals("Should have 3 rollback records", 3, records.size());
    }

    @Test
    public void testRollbackTypes() {
        Long processId = 32349L;

        // Test specific type rollback
        RollbackRecordEntity specificEntity = new RollbackRecordEntity();
        specificEntity.setId(4005L);
        specificEntity.setGmtCreate(DateUtil.getCurrentDate());
        specificEntity.setGmtModified(DateUtil.getCurrentDate());
        specificEntity.setProcessInstanceId(processId);
        specificEntity.setTaskInstanceId(42351L);
        specificEntity.setRollbackType("specific");
        specificEntity.setFromActivityId("userTask3");
        specificEntity.setToActivityId("userTask1");
        specificEntity.setOperatorUserId("operator006");
        specificEntity.setRollbackReason("Specific node rollback");
        specificEntity.setTenantId(TENANT_ID);
        rollbackRecordDAO.insert(specificEntity);

        // Test previous type rollback
        RollbackRecordEntity previousEntity = new RollbackRecordEntity();
        previousEntity.setId(4006L);
        previousEntity.setGmtCreate(DateUtil.getCurrentDate());
        previousEntity.setGmtModified(DateUtil.getCurrentDate());
        previousEntity.setProcessInstanceId(processId);
        previousEntity.setTaskInstanceId(42352L);
        previousEntity.setRollbackType("previous");
        previousEntity.setFromActivityId("userTask2");
        previousEntity.setToActivityId("userTask1");
        previousEntity.setOperatorUserId("operator006");
        previousEntity.setRollbackReason("Rollback to previous step");
        previousEntity.setTenantId(TENANT_ID);
        rollbackRecordDAO.insert(previousEntity);

        // Query and verify
        List<RollbackRecordEntity> records = rollbackRecordDAO.selectByProcessInstanceId(processId, TENANT_ID);
        Assert.assertEquals("Should have 2 rollback records", 2, records.size());

        // Verify different rollback types
        long specificCount = records.stream()
            .filter(r -> "specific".equals(r.getRollbackType()))
            .count();
        long previousCount = records.stream()
            .filter(r -> "previous".equals(r.getRollbackType()))
            .count();
        Assert.assertEquals("Should have 1 specific rollback", 1, specificCount);
        Assert.assertEquals("Should have 1 previous rollback", 1, previousCount);
    }

    @Test
    public void testDelete() {
        rollbackRecordDAO.insert(entity);

        RollbackRecordEntity retrieved = rollbackRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertNotNull("Record should exist before delete", retrieved);

        rollbackRecordDAO.delete(entity.getId(), TENANT_ID);

        retrieved = rollbackRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertNull("Record should be deleted", retrieved);
    }

    @Test
    public void testUpdate() {
        rollbackRecordDAO.insert(entity);

        RollbackRecordEntity retrieved = rollbackRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertNotNull(retrieved.getRollbackReason());

        entity.setRollbackReason("Updated rollback reason: data audit failed, need to refill");
        rollbackRecordDAO.update(entity);

        retrieved = rollbackRecordDAO.select(entity.getId(), TENANT_ID);
        Assert.assertEquals("Rollback reason should be updated",
            "Updated rollback reason: data audit failed, need to refill", retrieved.getRollbackReason());
    }
}
