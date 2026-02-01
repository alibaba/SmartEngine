package com.alibaba.smart.framework.engine.test.service;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.AssigneeOperationRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.BaseElementTest;
import com.alibaba.smart.framework.engine.persister.database.dao.RollbackRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskTransferRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.AssigneeOperationRecordEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.RollbackRecordEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskTransferRecordEntity;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Task operation record integration test
 * Tests transfer, rollback, add/remove assignee operations
 *
 * @author SmartEngine Team
 */
public class TaskOperationRecordIntegrationTest extends BaseElementTest {

    @Setter(onMethod = @__({@Autowired}))
    private TaskTransferRecordDAO taskTransferRecordDAO;

    @Setter(onMethod = @__({@Autowired}))
    private AssigneeOperationRecordDAO assigneeOperationRecordDAO;

    @Setter(onMethod = @__({@Autowired}))
    private RollbackRecordDAO rollbackRecordDAO;

    private static final String TENANT_ID = "-3";

    // ID counters for unique IDs
    private long transferIdCounter = 8001L;
    private long assigneeOpIdCounter = 8101L;
    private long rollbackIdCounter = 8201L;

    @Test
    public void testTaskTransferWithReason() {
        String fromUserId = "user001";
        String toUserId = "user002";
        Long taskInstanceId = 100001L;
        String reason = "Workload too heavy, need to transfer to colleague";

        TaskTransferRecordEntity entity = new TaskTransferRecordEntity();
        entity.setId(transferIdCounter++);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setTaskInstanceId(taskInstanceId);
        entity.setFromUserId(fromUserId);
        entity.setToUserId(toUserId);
        entity.setTransferReason(reason);
        entity.setTenantId(TENANT_ID);

        taskTransferRecordDAO.insert(entity);
        Assert.assertNotNull("Transfer record should be created", entity.getId());

        // Verify
        List<TaskTransferRecordEntity> records = taskTransferRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        Assert.assertFalse("Should have transfer records", records.isEmpty());
        Assert.assertEquals("Transfer reason should match", reason, records.get(0).getTransferReason());
        Assert.assertEquals("From user should match", fromUserId, records.get(0).getFromUserId());
        Assert.assertEquals("To user should match", toUserId, records.get(0).getToUserId());
    }

    @Test
    public void testMultipleTransfers() {
        Long taskInstanceId = 100002L;

        // First transfer: user001 -> user002
        TaskTransferRecordEntity transfer1 = new TaskTransferRecordEntity();
        transfer1.setId(transferIdCounter++);
        transfer1.setGmtCreate(DateUtil.getCurrentDate());
        transfer1.setGmtModified(DateUtil.getCurrentDate());
        transfer1.setTaskInstanceId(taskInstanceId);
        transfer1.setFromUserId("user001");
        transfer1.setToUserId("user002");
        transfer1.setTransferReason("First transfer");
        transfer1.setTenantId(TENANT_ID);
        taskTransferRecordDAO.insert(transfer1);

        // Second transfer: user002 -> user003
        TaskTransferRecordEntity transfer2 = new TaskTransferRecordEntity();
        transfer2.setId(transferIdCounter++);
        transfer2.setGmtCreate(DateUtil.getCurrentDate());
        transfer2.setGmtModified(DateUtil.getCurrentDate());
        transfer2.setTaskInstanceId(taskInstanceId);
        transfer2.setFromUserId("user002");
        transfer2.setToUserId("user003");
        transfer2.setTransferReason("Second transfer");
        transfer2.setTenantId(TENANT_ID);
        taskTransferRecordDAO.insert(transfer2);

        // Query transfer chain
        List<TaskTransferRecordEntity> transferChain = taskTransferRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        Assert.assertEquals("Should have 2 transfer records", 2, transferChain.size());
    }

    @Test
    public void testAddAssigneeWithReason() {
        Long taskInstanceId = 100003L;
        String operatorUserId = "manager001";
        String targetUserId = "expert001";
        String reason = "Need expert review";

        AssigneeOperationRecordEntity entity = new AssigneeOperationRecordEntity();
        entity.setId(assigneeOpIdCounter++);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setTaskInstanceId(taskInstanceId);
        entity.setOperationType("add_assignee");
        entity.setOperatorUserId(operatorUserId);
        entity.setTargetUserId(targetUserId);
        entity.setOperationReason(reason);
        entity.setTenantId(TENANT_ID);

        assigneeOperationRecordDAO.insert(entity);
        Assert.assertNotNull("Add assignee record should be created", entity.getId());

        // Verify
        List<AssigneeOperationRecordEntity> records = assigneeOperationRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        Assert.assertFalse("Should have operation records", records.isEmpty());
        Assert.assertEquals("Operation type should be add_assignee", "add_assignee", records.get(0).getOperationType());
        Assert.assertEquals("Target user should match", targetUserId, records.get(0).getTargetUserId());
    }

    @Test
    public void testRemoveAssigneeWithReason() {
        Long taskInstanceId = 100004L;
        String operatorUserId = "manager002";
        String targetUserId = "user005";
        String reason = "Employee has left";

        AssigneeOperationRecordEntity entity = new AssigneeOperationRecordEntity();
        entity.setId(assigneeOpIdCounter++);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setTaskInstanceId(taskInstanceId);
        entity.setOperationType("remove_assignee");
        entity.setOperatorUserId(operatorUserId);
        entity.setTargetUserId(targetUserId);
        entity.setOperationReason(reason);
        entity.setTenantId(TENANT_ID);

        assigneeOperationRecordDAO.insert(entity);
        Assert.assertNotNull("Remove assignee record should be created", entity.getId());

        // Verify
        List<AssigneeOperationRecordEntity> records = assigneeOperationRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        Assert.assertFalse("Should have operation records", records.isEmpty());
        Assert.assertEquals("Operation type should be remove_assignee", "remove_assignee", records.get(0).getOperationType());
        Assert.assertEquals("Operation reason should match", reason, records.get(0).getOperationReason());
    }

    @Test
    public void testAddAndRemoveAssigneeSequence() {
        Long taskInstanceId = 100005L;

        // Add expert1
        AssigneeOperationRecordEntity add1 = new AssigneeOperationRecordEntity();
        add1.setId(assigneeOpIdCounter++);
        add1.setGmtCreate(DateUtil.getCurrentDate());
        add1.setGmtModified(DateUtil.getCurrentDate());
        add1.setTaskInstanceId(taskInstanceId);
        add1.setOperationType("add_assignee");
        add1.setOperatorUserId("manager003");
        add1.setTargetUserId("expert002");
        add1.setOperationReason("Need finance expert review");
        add1.setTenantId(TENANT_ID);
        assigneeOperationRecordDAO.insert(add1);

        // Add expert2
        AssigneeOperationRecordEntity add2 = new AssigneeOperationRecordEntity();
        add2.setId(assigneeOpIdCounter++);
        add2.setGmtCreate(DateUtil.getCurrentDate());
        add2.setGmtModified(DateUtil.getCurrentDate());
        add2.setTaskInstanceId(taskInstanceId);
        add2.setOperationType("add_assignee");
        add2.setOperatorUserId("manager003");
        add2.setTargetUserId("expert003");
        add2.setOperationReason("Need legal expert review");
        add2.setTenantId(TENANT_ID);
        assigneeOperationRecordDAO.insert(add2);

        // Remove expert1 (review completed)
        AssigneeOperationRecordEntity remove1 = new AssigneeOperationRecordEntity();
        remove1.setId(assigneeOpIdCounter++);
        remove1.setGmtCreate(DateUtil.getCurrentDate());
        remove1.setGmtModified(DateUtil.getCurrentDate());
        remove1.setTaskInstanceId(taskInstanceId);
        remove1.setOperationType("remove_assignee");
        remove1.setOperatorUserId("manager003");
        remove1.setTargetUserId("expert002");
        remove1.setOperationReason("Finance review completed");
        remove1.setTenantId(TENANT_ID);
        assigneeOperationRecordDAO.insert(remove1);

        // Query all operation records
        List<AssigneeOperationRecordEntity> records = assigneeOperationRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        Assert.assertEquals("Should have 3 operation records", 3, records.size());

        // Count operation types
        long addCount = records.stream().filter(r -> "add_assignee".equals(r.getOperationType())).count();
        long removeCount = records.stream().filter(r -> "remove_assignee".equals(r.getOperationType())).count();
        Assert.assertEquals("Should have 2 add operations", 2, addCount);
        Assert.assertEquals("Should have 1 remove operation", 1, removeCount);
    }

    @Test
    public void testRollbackWithReason() {
        Long processInstanceId = 200001L;
        Long taskInstanceId = 100006L;
        String fromActivityId = "userTask2";
        String toActivityId = "userTask1";
        String operatorUserId = "approver001";
        String reason = "Found error in previous step, need to rollback";

        RollbackRecordEntity entity = new RollbackRecordEntity();
        entity.setId(rollbackIdCounter++);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setProcessInstanceId(processInstanceId);
        entity.setTaskInstanceId(taskInstanceId);
        entity.setRollbackType("specific");
        entity.setFromActivityId(fromActivityId);
        entity.setToActivityId(toActivityId);
        entity.setOperatorUserId(operatorUserId);
        entity.setRollbackReason(reason);
        entity.setTenantId(TENANT_ID);

        rollbackRecordDAO.insert(entity);
        Assert.assertNotNull("Rollback record should be created", entity.getId());

        // Verify
        List<RollbackRecordEntity> records = rollbackRecordDAO.selectByProcessInstanceId(processInstanceId, TENANT_ID);
        Assert.assertFalse("Should have rollback records", records.isEmpty());
        Assert.assertEquals("Rollback reason should match", reason, records.get(0).getRollbackReason());
        Assert.assertEquals("From activity should match", fromActivityId, records.get(0).getFromActivityId());
        Assert.assertEquals("To activity should match", toActivityId, records.get(0).getToActivityId());
        Assert.assertEquals("Operator should match", operatorUserId, records.get(0).getOperatorUserId());
    }

    @Test
    public void testMultipleRollbacks() {
        Long processInstanceId = 200002L;

        // First rollback: task3 -> task2
        RollbackRecordEntity rollback1 = new RollbackRecordEntity();
        rollback1.setId(rollbackIdCounter++);
        rollback1.setGmtCreate(DateUtil.getCurrentDate());
        rollback1.setGmtModified(DateUtil.getCurrentDate());
        rollback1.setProcessInstanceId(processInstanceId);
        rollback1.setTaskInstanceId(100007L);
        rollback1.setRollbackType("previous");
        rollback1.setFromActivityId("userTask3");
        rollback1.setToActivityId("userTask2");
        rollback1.setOperatorUserId("approver002");
        rollback1.setRollbackReason("First rollback: approval rejected");
        rollback1.setTenantId(TENANT_ID);
        rollbackRecordDAO.insert(rollback1);

        // Second rollback: task2 -> task1
        RollbackRecordEntity rollback2 = new RollbackRecordEntity();
        rollback2.setId(rollbackIdCounter++);
        rollback2.setGmtCreate(DateUtil.getCurrentDate());
        rollback2.setGmtModified(DateUtil.getCurrentDate());
        rollback2.setProcessInstanceId(processInstanceId);
        rollback2.setTaskInstanceId(100008L);
        rollback2.setRollbackType("specific");
        rollback2.setFromActivityId("userTask2");
        rollback2.setToActivityId("userTask1");
        rollback2.setOperatorUserId("approver002");
        rollback2.setRollbackReason("Second rollback: need to restart");
        rollback2.setTenantId(TENANT_ID);
        rollbackRecordDAO.insert(rollback2);

        // Query rollback history
        List<RollbackRecordEntity> rollbackHistory = rollbackRecordDAO.selectByProcessInstanceId(processInstanceId, TENANT_ID);
        Assert.assertEquals("Should have 2 rollback records", 2, rollbackHistory.size());
    }

    @Test
    public void testOperationRecordAuditTrail() {
        Long taskInstanceId = 100009L;
        Long processInstanceId = 200003L;

        // 1. Task transfer
        TaskTransferRecordEntity transfer = new TaskTransferRecordEntity();
        transfer.setId(transferIdCounter++);
        transfer.setGmtCreate(DateUtil.getCurrentDate());
        transfer.setGmtModified(DateUtil.getCurrentDate());
        transfer.setTaskInstanceId(taskInstanceId);
        transfer.setFromUserId("user001");
        transfer.setToUserId("user002");
        transfer.setTransferReason("Transfer to professional");
        transfer.setTenantId(TENANT_ID);
        taskTransferRecordDAO.insert(transfer);

        // 2. Add assignee
        AssigneeOperationRecordEntity addAssignee = new AssigneeOperationRecordEntity();
        addAssignee.setId(assigneeOpIdCounter++);
        addAssignee.setGmtCreate(DateUtil.getCurrentDate());
        addAssignee.setGmtModified(DateUtil.getCurrentDate());
        addAssignee.setTaskInstanceId(taskInstanceId);
        addAssignee.setOperationType("add_assignee");
        addAssignee.setOperatorUserId("manager004");
        addAssignee.setTargetUserId("approver003");
        addAssignee.setOperationReason("Add department manager for approval");
        addAssignee.setTenantId(TENANT_ID);
        assigneeOperationRecordDAO.insert(addAssignee);

        // 3. Rollback
        RollbackRecordEntity rollback = new RollbackRecordEntity();
        rollback.setId(rollbackIdCounter++);
        rollback.setGmtCreate(DateUtil.getCurrentDate());
        rollback.setGmtModified(DateUtil.getCurrentDate());
        rollback.setProcessInstanceId(processInstanceId);
        rollback.setTaskInstanceId(taskInstanceId);
        rollback.setRollbackType("previous");
        rollback.setFromActivityId("approvalTask");
        rollback.setToActivityId("fillFormTask");
        rollback.setOperatorUserId("approver003");
        rollback.setRollbackReason("Form incomplete");
        rollback.setTenantId(TENANT_ID);
        rollbackRecordDAO.insert(rollback);

        // Verify all operations have records
        List<TaskTransferRecordEntity> transfers = taskTransferRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        List<AssigneeOperationRecordEntity> operations = assigneeOperationRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        List<RollbackRecordEntity> rollbacks = rollbackRecordDAO.selectByProcessInstanceId(processInstanceId, TENANT_ID);

        Assert.assertEquals("Should have 1 transfer record", 1, transfers.size());
        Assert.assertEquals("Should have 1 assignee operation record", 1, operations.size());
        Assert.assertEquals("Should have 1 rollback record", 1, rollbacks.size());

        // Verify audit trail completeness
        Assert.assertNotNull("Transfer reason should be recorded", transfers.get(0).getTransferReason());
        Assert.assertNotNull("Operation reason should be recorded", operations.get(0).getOperationReason());
        Assert.assertNotNull("Rollback reason should be recorded", rollbacks.get(0).getRollbackReason());
    }
}
