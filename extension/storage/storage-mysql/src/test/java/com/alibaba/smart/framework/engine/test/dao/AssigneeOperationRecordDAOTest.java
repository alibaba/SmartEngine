package com.alibaba.smart.framework.engine.test.dao;

import com.alibaba.smart.framework.engine.persister.database.dao.AssigneeOperationRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.AssigneeOperationRecordEntity;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * AssigneeOperationRecordDAO单元测试
 *
 * @author SmartEngine Team
 */
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AssigneeOperationRecordDAOTest extends DatabaseBaseTestCase {

    @Autowired
    private AssigneeOperationRecordDAO assigneeOperationRecordDAO;

    @Test
    public void testInsertAndSelect() {
        // 创建加签记录
        AssigneeOperationRecordEntity entity = new AssigneeOperationRecordEntity();
        entity.setGmtCreate(new Date());
        entity.setGmtModified(new Date());
        entity.setTaskInstanceId(22345L);
        entity.setOperationType("add_assignee");
        entity.setOperatorUserId("manager001");
        entity.setTargetUserId("user011");
        entity.setOperationReason("需要增加技术专家进行审核");
        entity.setTenantId("tenant001");

        // 插入记录
        assigneeOperationRecordDAO.insert(entity);
        assertNotNull("ID should be auto-generated", entity.getId());

        // 查询记录
        AssigneeOperationRecordEntity retrieved = assigneeOperationRecordDAO.select(entity.getId(), "tenant001");
        assertNotNull("Retrieved entity should not be null", retrieved);
        assertEquals("Task instance ID should match", Long.valueOf(22345L), retrieved.getTaskInstanceId());
        assertEquals("Operation type should match", "add_assignee", retrieved.getOperationType());
        assertEquals("Operator user ID should match", "manager001", retrieved.getOperatorUserId());
        assertEquals("Target user ID should match", "user011", retrieved.getTargetUserId());
        assertEquals("Operation reason should match", "需要增加技术专家进行审核", retrieved.getOperationReason());
    }

    @Test
    public void testUpdate() {
        // 插入初始记录
        AssigneeOperationRecordEntity entity = new AssigneeOperationRecordEntity();
        entity.setGmtCreate(new Date());
        entity.setGmtModified(new Date());
        entity.setTaskInstanceId(22346L);
        entity.setOperationType("remove_assignee");
        entity.setOperatorUserId("manager002");
        entity.setTargetUserId("user012");
        entity.setOperationReason("初始原因");
        entity.setTenantId("tenant001");
        assigneeOperationRecordDAO.insert(entity);

        // 更新记录
        entity.setOperationReason("更新后的减签原因：该人员已离职");
        assigneeOperationRecordDAO.update(entity);

        // 验证更新
        AssigneeOperationRecordEntity retrieved = assigneeOperationRecordDAO.select(entity.getId(), "tenant001");
        assertEquals("Operation reason should be updated", "更新后的减签原因：该人员已离职", retrieved.getOperationReason());
    }

    @Test
    public void testSelectByTaskInstanceId() {
        Long taskInstanceId = 22347L;
        String tenantId = "tenant001";

        // 插入加签记录
        AssigneeOperationRecordEntity addEntity1 = new AssigneeOperationRecordEntity();
        addEntity1.setGmtCreate(new Date());
        addEntity1.setGmtModified(new Date());
        addEntity1.setTaskInstanceId(taskInstanceId);
        addEntity1.setOperationType("add_assignee");
        addEntity1.setOperatorUserId("manager003");
        addEntity1.setTargetUserId("user013");
        addEntity1.setOperationReason("加签原因1");
        addEntity1.setTenantId(tenantId);
        assigneeOperationRecordDAO.insert(addEntity1);

        // 插入第二条加签记录
        AssigneeOperationRecordEntity addEntity2 = new AssigneeOperationRecordEntity();
        addEntity2.setGmtCreate(new Date());
        addEntity2.setGmtModified(new Date());
        addEntity2.setTaskInstanceId(taskInstanceId);
        addEntity2.setOperationType("add_assignee");
        addEntity2.setOperatorUserId("manager003");
        addEntity2.setTargetUserId("user014");
        addEntity2.setOperationReason("加签原因2");
        addEntity2.setTenantId(tenantId);
        assigneeOperationRecordDAO.insert(addEntity2);

        // 插入减签记录
        AssigneeOperationRecordEntity removeEntity = new AssigneeOperationRecordEntity();
        removeEntity.setGmtCreate(new Date());
        removeEntity.setGmtModified(new Date());
        removeEntity.setTaskInstanceId(taskInstanceId);
        removeEntity.setOperationType("remove_assignee");
        removeEntity.setOperatorUserId("manager003");
        removeEntity.setTargetUserId("user013");
        removeEntity.setOperationReason("减签原因");
        removeEntity.setTenantId(tenantId);
        assigneeOperationRecordDAO.insert(removeEntity);

        // 查询任务的所有加签减签记录
        List<AssigneeOperationRecordEntity> records = assigneeOperationRecordDAO.selectByTaskInstanceId(taskInstanceId, tenantId);
        assertNotNull("Records should not be null", records);
        assertEquals("Should have 3 operation records", 3, records.size());

        // 验证包含不同类型的操作
        long addCount = records.stream()
            .filter(r -> "add_assignee".equals(r.getOperationType()))
            .count();
        long removeCount = records.stream()
            .filter(r -> "remove_assignee".equals(r.getOperationType()))
            .count();
        assertEquals("Should have 2 add operations", 2, addCount);
        assertEquals("Should have 1 remove operation", 1, removeCount);
    }

    @Test
    public void testDelete() {
        // 插入记录
        AssigneeOperationRecordEntity entity = new AssigneeOperationRecordEntity();
        entity.setGmtCreate(new Date());
        entity.setGmtModified(new Date());
        entity.setTaskInstanceId(22348L);
        entity.setOperationType("add_assignee");
        entity.setOperatorUserId("manager004");
        entity.setTargetUserId("user015");
        entity.setOperationReason("待删除的记录");
        entity.setTenantId("tenant001");
        assigneeOperationRecordDAO.insert(entity);

        Long recordId = entity.getId();
        assertNotNull("Record ID should exist", recordId);

        // 删除记录
        assigneeOperationRecordDAO.delete(recordId, "tenant001");

        // 验证已删除
        AssigneeOperationRecordEntity retrieved = assigneeOperationRecordDAO.select(recordId, "tenant001");
        assertNull("Record should be deleted", retrieved);
    }

    @Test
    public void testOperationTypeValidation() {
        // 测试加签操作
        AssigneeOperationRecordEntity addEntity = new AssigneeOperationRecordEntity();
        addEntity.setGmtCreate(new Date());
        addEntity.setGmtModified(new Date());
        addEntity.setTaskInstanceId(22349L);
        addEntity.setOperationType("add_assignee");
        addEntity.setOperatorUserId("manager005");
        addEntity.setTargetUserId("user016");
        addEntity.setOperationReason("加签测试");
        addEntity.setTenantId("tenant001");
        assigneeOperationRecordDAO.insert(addEntity);

        AssigneeOperationRecordEntity retrieved = assigneeOperationRecordDAO.select(addEntity.getId(), "tenant001");
        assertEquals("Operation type should be add_assignee", "add_assignee", retrieved.getOperationType());

        // 测试减签操作
        AssigneeOperationRecordEntity removeEntity = new AssigneeOperationRecordEntity();
        removeEntity.setGmtCreate(new Date());
        removeEntity.setGmtModified(new Date());
        removeEntity.setTaskInstanceId(22349L);
        removeEntity.setOperationType("remove_assignee");
        removeEntity.setOperatorUserId("manager005");
        removeEntity.setTargetUserId("user016");
        removeEntity.setOperationReason("减签测试");
        removeEntity.setTenantId("tenant001");
        assigneeOperationRecordDAO.insert(removeEntity);

        AssigneeOperationRecordEntity retrieved2 = assigneeOperationRecordDAO.select(removeEntity.getId(), "tenant001");
        assertEquals("Operation type should be remove_assignee", "remove_assignee", retrieved2.getOperationType());
    }
}
