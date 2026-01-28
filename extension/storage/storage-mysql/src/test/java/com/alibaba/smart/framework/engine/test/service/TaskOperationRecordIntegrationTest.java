package com.alibaba.smart.framework.engine.test.service;

import com.alibaba.smart.framework.engine.persister.database.dao.AssigneeOperationRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.RollbackRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskTransferRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.AssigneeOperationRecordEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.RollbackRecordEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskTransferRecordEntity;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 任务操作记录集成测试
 * 测试移交、回退、加签、减签操作的记录功能
 *
 * @author SmartEngine Team
 */
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TaskOperationRecordIntegrationTest extends DatabaseBaseTestCase {

    @Autowired
    private TaskTransferRecordDAO taskTransferRecordDAO;

    @Autowired
    private AssigneeOperationRecordDAO assigneeOperationRecordDAO;

    @Autowired
    private RollbackRecordDAO rollbackRecordDAO;

    private static final String TENANT_ID = "test-tenant";

    @Before
    public void deployProcess() {
        // 如果已部署则跳过
        // 实际测试时需要部署一个简单的测试流程
    }

    @Test
    public void testTaskTransferWithReason() {
        // 此测试验证任务移交记录功能
        // 由于需要完整的流程上下文，这里只验证DAO层功能

        String fromUserId = "user001";
        String toUserId = "user002";
        Long taskInstanceId = 100001L;
        String reason = "工作量过大，需要移交给其他同事处理";

        // 模拟移交操作后的记录保存
        TaskTransferRecordEntity entity = new TaskTransferRecordEntity();
        entity.setGmtCreate(new java.util.Date());
        entity.setGmtModified(new java.util.Date());
        entity.setTaskInstanceId(taskInstanceId);
        entity.setFromUserId(fromUserId);
        entity.setToUserId(toUserId);
        entity.setTransferReason(reason);
        entity.setTenantId(TENANT_ID);

        taskTransferRecordDAO.insert(entity);
        assertNotNull("Transfer record should be created", entity.getId());

        // 查询验证
        List<TaskTransferRecordEntity> records = taskTransferRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        assertFalse("Should have transfer records", records.isEmpty());
        assertEquals("Transfer reason should match", reason, records.get(0).getTransferReason());
        assertEquals("From user should match", fromUserId, records.get(0).getFromUserId());
        assertEquals("To user should match", toUserId, records.get(0).getToUserId());
    }

    @Test
    public void testMultipleTransfers() {
        // 测试任务多次移交的场景
        Long taskInstanceId = 100002L;

        // 第一次移交：user001 -> user002
        TaskTransferRecordEntity transfer1 = new TaskTransferRecordEntity();
        transfer1.setGmtCreate(new java.util.Date());
        transfer1.setGmtModified(new java.util.Date());
        transfer1.setTaskInstanceId(taskInstanceId);
        transfer1.setFromUserId("user001");
        transfer1.setToUserId("user002");
        transfer1.setTransferReason("初次移交");
        transfer1.setTenantId(TENANT_ID);
        taskTransferRecordDAO.insert(transfer1);

        // 第二次移交：user002 -> user003
        TaskTransferRecordEntity transfer2 = new TaskTransferRecordEntity();
        transfer2.setGmtCreate(new java.util.Date());
        transfer2.setGmtModified(new java.util.Date());
        transfer2.setTaskInstanceId(taskInstanceId);
        transfer2.setFromUserId("user002");
        transfer2.setToUserId("user003");
        transfer2.setTransferReason("二次移交");
        transfer2.setTenantId(TENANT_ID);
        taskTransferRecordDAO.insert(transfer2);

        // 查询移交链
        List<TaskTransferRecordEntity> transferChain = taskTransferRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        assertEquals("Should have 2 transfer records", 2, transferChain.size());

        // 验证移交链的顺序（最新的在前）
        assertEquals("Most recent transfer should be first", "user003", transferChain.get(0).getToUserId());
        assertEquals("Original transfer should be last", "user002", transferChain.get(1).getToUserId());
    }

    @Test
    public void testAddAssigneeWithReason() {
        // 测试加签操作记录
        Long taskInstanceId = 100003L;
        String operatorUserId = "manager001";
        String targetUserId = "expert001";
        String reason = "需要专家审核";

        // 模拟加签操作后的记录保存
        AssigneeOperationRecordEntity entity = new AssigneeOperationRecordEntity();
        entity.setGmtCreate(new java.util.Date());
        entity.setGmtModified(new java.util.Date());
        entity.setTaskInstanceId(taskInstanceId);
        entity.setOperationType("add_assignee");
        entity.setOperatorUserId(operatorUserId);
        entity.setTargetUserId(targetUserId);
        entity.setOperationReason(reason);
        entity.setTenantId(TENANT_ID);

        assigneeOperationRecordDAO.insert(entity);
        assertNotNull("Add assignee record should be created", entity.getId());

        // 查询验证
        List<AssigneeOperationRecordEntity> records = assigneeOperationRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        assertFalse("Should have operation records", records.isEmpty());
        assertEquals("Operation type should be add_assignee", "add_assignee", records.get(0).getOperationType());
        assertEquals("Target user should match", targetUserId, records.get(0).getTargetUserId());
    }

    @Test
    public void testRemoveAssigneeWithReason() {
        // 测试减签操作记录
        Long taskInstanceId = 100004L;
        String operatorUserId = "manager002";
        String targetUserId = "user005";
        String reason = "该人员已离职";

        // 模拟减签操作后的记录保存
        AssigneeOperationRecordEntity entity = new AssigneeOperationRecordEntity();
        entity.setGmtCreate(new java.util.Date());
        entity.setGmtModified(new java.util.Date());
        entity.setTaskInstanceId(taskInstanceId);
        entity.setOperationType("remove_assignee");
        entity.setOperatorUserId(operatorUserId);
        entity.setTargetUserId(targetUserId);
        entity.setOperationReason(reason);
        entity.setTenantId(TENANT_ID);

        assigneeOperationRecordDAO.insert(entity);
        assertNotNull("Remove assignee record should be created", entity.getId());

        // 查询验证
        List<AssigneeOperationRecordEntity> records = assigneeOperationRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        assertFalse("Should have operation records", records.isEmpty());
        assertEquals("Operation type should be remove_assignee", "remove_assignee", records.get(0).getOperationType());
        assertEquals("Operation reason should match", reason, records.get(0).getOperationReason());
    }

    @Test
    public void testAddAndRemoveAssigneeSequence() {
        // 测试先加签后减签的完整流程
        Long taskInstanceId = 100005L;

        // 加签专家1
        AssigneeOperationRecordEntity add1 = new AssigneeOperationRecordEntity();
        add1.setGmtCreate(new java.util.Date());
        add1.setGmtModified(new java.util.Date());
        add1.setTaskInstanceId(taskInstanceId);
        add1.setOperationType("add_assignee");
        add1.setOperatorUserId("manager003");
        add1.setTargetUserId("expert002");
        add1.setOperationReason("需要财务专家审核");
        add1.setTenantId(TENANT_ID);
        assigneeOperationRecordDAO.insert(add1);

        // 加签专家2
        AssigneeOperationRecordEntity add2 = new AssigneeOperationRecordEntity();
        add2.setGmtCreate(new java.util.Date());
        add2.setGmtModified(new java.util.Date());
        add2.setTaskInstanceId(taskInstanceId);
        add2.setOperationType("add_assignee");
        add2.setOperatorUserId("manager003");
        add2.setTargetUserId("expert003");
        add2.setOperationReason("需要法务专家审核");
        add2.setTenantId(TENANT_ID);
        assigneeOperationRecordDAO.insert(add2);

        // 减签专家1（已完成审核）
        AssigneeOperationRecordEntity remove1 = new AssigneeOperationRecordEntity();
        remove1.setGmtCreate(new java.util.Date());
        remove1.setGmtModified(new java.util.Date());
        remove1.setTaskInstanceId(taskInstanceId);
        remove1.setOperationType("remove_assignee");
        remove1.setOperatorUserId("manager003");
        remove1.setTargetUserId("expert002");
        remove1.setOperationReason("财务审核已完成");
        remove1.setTenantId(TENANT_ID);
        assigneeOperationRecordDAO.insert(remove1);

        // 查询所有操作记录
        List<AssigneeOperationRecordEntity> records = assigneeOperationRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        assertEquals("Should have 3 operation records", 3, records.size());

        // 统计操作类型
        long addCount = records.stream().filter(r -> "add_assignee".equals(r.getOperationType())).count();
        long removeCount = records.stream().filter(r -> "remove_assignee".equals(r.getOperationType())).count();
        assertEquals("Should have 2 add operations", 2, addCount);
        assertEquals("Should have 1 remove operation", 1, removeCount);
    }

    @Test
    public void testRollbackWithReason() {
        // 测试流程回退记录
        Long processInstanceId = 200001L;
        Long taskInstanceId = 100006L;
        String fromActivityId = "userTask2";
        String toActivityId = "userTask1";
        String operatorUserId = "approver001";
        String reason = "发现上一步填写有误，需要回退重新填写";

        // 模拟回退操作后的记录保存
        RollbackRecordEntity entity = new RollbackRecordEntity();
        entity.setGmtCreate(new java.util.Date());
        entity.setGmtModified(new java.util.Date());
        entity.setProcessInstanceId(processInstanceId);
        entity.setTaskInstanceId(taskInstanceId);
        entity.setRollbackType("specific");
        entity.setFromActivityId(fromActivityId);
        entity.setToActivityId(toActivityId);
        entity.setOperatorUserId(operatorUserId);
        entity.setRollbackReason(reason);
        entity.setTenantId(TENANT_ID);

        rollbackRecordDAO.insert(entity);
        assertNotNull("Rollback record should be created", entity.getId());

        // 查询验证
        List<RollbackRecordEntity> records = rollbackRecordDAO.selectByProcessInstanceId(processInstanceId, TENANT_ID);
        assertFalse("Should have rollback records", records.isEmpty());
        assertEquals("Rollback reason should match", reason, records.get(0).getRollbackReason());
        assertEquals("From activity should match", fromActivityId, records.get(0).getFromActivityId());
        assertEquals("To activity should match", toActivityId, records.get(0).getToActivityId());
        assertEquals("Operator should match", operatorUserId, records.get(0).getOperatorUserId());
    }

    @Test
    public void testMultipleRollbacks() {
        // 测试流程多次回退的场景
        Long processInstanceId = 200002L;

        // 第一次回退：task3 -> task2
        RollbackRecordEntity rollback1 = new RollbackRecordEntity();
        rollback1.setGmtCreate(new java.util.Date());
        rollback1.setGmtModified(new java.util.Date());
        rollback1.setProcessInstanceId(processInstanceId);
        rollback1.setTaskInstanceId(100007L);
        rollback1.setRollbackType("previous");
        rollback1.setFromActivityId("userTask3");
        rollback1.setToActivityId("userTask2");
        rollback1.setOperatorUserId("approver002");
        rollback1.setRollbackReason("第一次回退：审核不通过");
        rollback1.setTenantId(TENANT_ID);
        rollbackRecordDAO.insert(rollback1);

        // 第二次回退：task2 -> task1（修正后仍有问题，再次回退）
        RollbackRecordEntity rollback2 = new RollbackRecordEntity();
        rollback2.setGmtCreate(new java.util.Date());
        rollback2.setGmtModified(new java.util.Date());
        rollback2.setProcessInstanceId(processInstanceId);
        rollback2.setTaskInstanceId(100008L);
        rollback2.setRollbackType("specific");
        rollback2.setFromActivityId("userTask2");
        rollback2.setToActivityId("userTask1");
        rollback2.setOperatorUserId("approver002");
        rollback2.setRollbackReason("第二次回退：需要从头开始");
        rollback2.setTenantId(TENANT_ID);
        rollbackRecordDAO.insert(rollback2);

        // 查询回退历史
        List<RollbackRecordEntity> rollbackHistory = rollbackRecordDAO.selectByProcessInstanceId(processInstanceId, TENANT_ID);
        assertEquals("Should have 2 rollback records", 2, rollbackHistory.size());

        // 验证按时间倒序
        assertTrue("Most recent rollback should mention task1",
            rollbackHistory.get(0).getToActivityId().contains("task1"));
    }

    @Test
    public void testOperationRecordAuditTrail() {
        // 综合测试：验证完整的操作审计链
        Long taskInstanceId = 100009L;
        Long processInstanceId = 200003L;

        // 1. 任务移交
        TaskTransferRecordEntity transfer = new TaskTransferRecordEntity();
        transfer.setGmtCreate(new java.util.Date());
        transfer.setGmtModified(new java.util.Date());
        transfer.setTaskInstanceId(taskInstanceId);
        transfer.setFromUserId("user001");
        transfer.setToUserId("user002");
        transfer.setTransferReason("移交给专业人员处理");
        transfer.setTenantId(TENANT_ID);
        taskTransferRecordDAO.insert(transfer);

        // 2. 加签审批人
        AssigneeOperationRecordEntity addAssignee = new AssigneeOperationRecordEntity();
        addAssignee.setGmtCreate(new java.util.Date());
        addAssignee.setGmtModified(new java.util.Date());
        addAssignee.setTaskInstanceId(taskInstanceId);
        addAssignee.setOperationType("add_assignee");
        addAssignee.setOperatorUserId("manager004");
        addAssignee.setTargetUserId("approver003");
        addAssignee.setOperationReason("添加部门经理审批");
        addAssignee.setTenantId(TENANT_ID);
        assigneeOperationRecordDAO.insert(addAssignee);

        // 3. 流程回退
        RollbackRecordEntity rollback = new RollbackRecordEntity();
        rollback.setGmtCreate(new java.util.Date());
        rollback.setGmtModified(new java.util.Date());
        rollback.setProcessInstanceId(processInstanceId);
        rollback.setTaskInstanceId(taskInstanceId);
        rollback.setRollbackType("previous");
        rollback.setFromActivityId("approvalTask");
        rollback.setToActivityId("fillFormTask");
        rollback.setOperatorUserId("approver003");
        rollback.setRollbackReason("表单填写不完整");
        rollback.setTenantId(TENANT_ID);
        rollbackRecordDAO.insert(rollback);

        // 验证所有操作都有记录
        List<TaskTransferRecordEntity> transfers = taskTransferRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        List<AssigneeOperationRecordEntity> operations = assigneeOperationRecordDAO.selectByTaskInstanceId(taskInstanceId, TENANT_ID);
        List<RollbackRecordEntity> rollbacks = rollbackRecordDAO.selectByProcessInstanceId(processInstanceId, TENANT_ID);

        assertEquals("Should have 1 transfer record", 1, transfers.size());
        assertEquals("Should have 1 assignee operation record", 1, operations.size());
        assertEquals("Should have 1 rollback record", 1, rollbacks.size());

        // 验证审计链的完整性
        assertNotNull("Transfer reason should be recorded", transfers.get(0).getTransferReason());
        assertNotNull("Operation reason should be recorded", operations.get(0).getOperationReason());
        assertNotNull("Rollback reason should be recorded", rollbacks.get(0).getRollbackReason());
    }
}
