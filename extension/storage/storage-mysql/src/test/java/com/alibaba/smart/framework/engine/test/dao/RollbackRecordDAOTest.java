package com.alibaba.smart.framework.engine.test.dao;

import com.alibaba.smart.framework.engine.persister.database.dao.RollbackRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.RollbackRecordEntity;
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
 * RollbackRecordDAO单元测试
 *
 * @author SmartEngine Team
 */
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RollbackRecordDAOTest extends DatabaseBaseTestCase {

    @Autowired
    private RollbackRecordDAO rollbackRecordDAO;

    @Test
    public void testInsertAndSelect() {
        // 创建回退记录
        RollbackRecordEntity entity = new RollbackRecordEntity();
        entity.setGmtCreate(new Date());
        entity.setGmtModified(new Date());
        entity.setProcessInstanceId(32345L);
        entity.setTaskInstanceId(42345L);
        entity.setRollbackType("specific");
        entity.setFromActivityId("userTask2");
        entity.setToActivityId("userTask1");
        entity.setOperatorUserId("operator001");
        entity.setRollbackReason("发现前一步骤有错误，需要回退修正");
        entity.setTenantId("tenant001");

        // 插入记录
        rollbackRecordDAO.insert(entity);
        assertNotNull("ID should be auto-generated", entity.getId());

        // 查询记录
        RollbackRecordEntity retrieved = rollbackRecordDAO.select(entity.getId(), "tenant001");
        assertNotNull("Retrieved entity should not be null", retrieved);
        assertEquals("Process instance ID should match", Long.valueOf(32345L), retrieved.getProcessInstanceId());
        assertEquals("Task instance ID should match", Long.valueOf(42345L), retrieved.getTaskInstanceId());
        assertEquals("Rollback type should match", "specific", retrieved.getRollbackType());
        assertEquals("From activity ID should match", "userTask2", retrieved.getFromActivityId());
        assertEquals("To activity ID should match", "userTask1", retrieved.getToActivityId());
        assertEquals("Operator user ID should match", "operator001", retrieved.getOperatorUserId());
        assertEquals("Rollback reason should match", "发现前一步骤有错误，需要回退修正", retrieved.getRollbackReason());
    }

    @Test
    public void testUpdate() {
        // 插入初始记录
        RollbackRecordEntity entity = new RollbackRecordEntity();
        entity.setGmtCreate(new Date());
        entity.setGmtModified(new Date());
        entity.setProcessInstanceId(32346L);
        entity.setTaskInstanceId(42346L);
        entity.setRollbackType("specific");
        entity.setFromActivityId("userTask3");
        entity.setToActivityId("userTask2");
        entity.setOperatorUserId("operator002");
        entity.setRollbackReason("初始原因");
        entity.setTenantId("tenant001");
        rollbackRecordDAO.insert(entity);

        // 更新记录
        entity.setRollbackReason("更新后的回退原因：数据审核不通过，需要重新填写");
        rollbackRecordDAO.update(entity);

        // 验证更新
        RollbackRecordEntity retrieved = rollbackRecordDAO.select(entity.getId(), "tenant001");
        assertEquals("Rollback reason should be updated", "更新后的回退原因：数据审核不通过，需要重新填写", retrieved.getRollbackReason());
    }

    @Test
    public void testSelectByProcessInstanceId() {
        Long processInstanceId = 32347L;
        String tenantId = "tenant001";

        // 插入多条回退记录，模拟流程多次回退的情况
        RollbackRecordEntity entity1 = new RollbackRecordEntity();
        entity1.setGmtCreate(new Date());
        entity1.setGmtModified(new Date());
        entity1.setProcessInstanceId(processInstanceId);
        entity1.setTaskInstanceId(42347L);
        entity1.setRollbackType("specific");
        entity1.setFromActivityId("userTask3");
        entity1.setToActivityId("userTask2");
        entity1.setOperatorUserId("operator003");
        entity1.setRollbackReason("第一次回退");
        entity1.setTenantId(tenantId);
        rollbackRecordDAO.insert(entity1);

        // 稍微延迟以确保时间戳不同
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // ignore
        }

        RollbackRecordEntity entity2 = new RollbackRecordEntity();
        entity2.setGmtCreate(new Date());
        entity2.setGmtModified(new Date());
        entity2.setProcessInstanceId(processInstanceId);
        entity2.setTaskInstanceId(42348L);
        entity2.setRollbackType("specific");
        entity2.setFromActivityId("userTask2");
        entity2.setToActivityId("userTask1");
        entity2.setOperatorUserId("operator003");
        entity2.setRollbackReason("第二次回退");
        entity2.setTenantId(tenantId);
        rollbackRecordDAO.insert(entity2);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // ignore
        }

        RollbackRecordEntity entity3 = new RollbackRecordEntity();
        entity3.setGmtCreate(new Date());
        entity3.setGmtModified(new Date());
        entity3.setProcessInstanceId(processInstanceId);
        entity3.setTaskInstanceId(42349L);
        entity3.setRollbackType("specific");
        entity3.setFromActivityId("userTask4");
        entity3.setToActivityId("userTask3");
        entity3.setOperatorUserId("operator004");
        entity3.setRollbackReason("第三次回退");
        entity3.setTenantId(tenantId);
        rollbackRecordDAO.insert(entity3);

        // 查询流程的所有回退记录
        List<RollbackRecordEntity> records = rollbackRecordDAO.selectByProcessInstanceId(processInstanceId, tenantId);
        assertNotNull("Records should not be null", records);
        assertEquals("Should have 3 rollback records", 3, records.size());

        // 验证按创建时间倒序排列（最新的在前面）
        assertEquals("First record should be the most recent", "第三次回退", records.get(0).getRollbackReason());
        assertEquals("Second record should be the middle one", "第二次回退", records.get(1).getRollbackReason());
        assertEquals("Third record should be the oldest", "第一次回退", records.get(2).getRollbackReason());
    }

    @Test
    public void testDelete() {
        // 插入记录
        RollbackRecordEntity entity = new RollbackRecordEntity();
        entity.setGmtCreate(new Date());
        entity.setGmtModified(new Date());
        entity.setProcessInstanceId(32348L);
        entity.setTaskInstanceId(42350L);
        entity.setRollbackType("specific");
        entity.setFromActivityId("userTask5");
        entity.setToActivityId("userTask4");
        entity.setOperatorUserId("operator005");
        entity.setRollbackReason("待删除的记录");
        entity.setTenantId("tenant001");
        rollbackRecordDAO.insert(entity);

        Long recordId = entity.getId();
        assertNotNull("Record ID should exist", recordId);

        // 删除记录
        rollbackRecordDAO.delete(recordId, "tenant001");

        // 验证已删除
        RollbackRecordEntity retrieved = rollbackRecordDAO.select(recordId, "tenant001");
        assertNull("Record should be deleted", retrieved);
    }

    @Test
    public void testRollbackTypes() {
        Long processInstanceId = 32349L;
        String tenantId = "tenant001";

        // 测试specific类型回退
        RollbackRecordEntity specificEntity = new RollbackRecordEntity();
        specificEntity.setGmtCreate(new Date());
        specificEntity.setGmtModified(new Date());
        specificEntity.setProcessInstanceId(processInstanceId);
        specificEntity.setTaskInstanceId(42351L);
        specificEntity.setRollbackType("specific");
        specificEntity.setFromActivityId("userTask3");
        specificEntity.setToActivityId("userTask1");
        specificEntity.setOperatorUserId("operator006");
        specificEntity.setRollbackReason("指定节点回退");
        specificEntity.setTenantId(tenantId);
        rollbackRecordDAO.insert(specificEntity);

        // 测试previous类型回退
        RollbackRecordEntity previousEntity = new RollbackRecordEntity();
        previousEntity.setGmtCreate(new Date());
        previousEntity.setGmtModified(new Date());
        previousEntity.setProcessInstanceId(processInstanceId);
        previousEntity.setTaskInstanceId(42352L);
        previousEntity.setRollbackType("previous");
        previousEntity.setFromActivityId("userTask2");
        previousEntity.setToActivityId("userTask1");
        previousEntity.setOperatorUserId("operator006");
        previousEntity.setRollbackReason("回退到上一步");
        previousEntity.setTenantId(tenantId);
        rollbackRecordDAO.insert(previousEntity);

        // 查询并验证
        List<RollbackRecordEntity> records = rollbackRecordDAO.selectByProcessInstanceId(processInstanceId, tenantId);
        assertEquals("Should have 2 rollback records", 2, records.size());

        // 验证不同回退类型
        long specificCount = records.stream()
            .filter(r -> "specific".equals(r.getRollbackType()))
            .count();
        long previousCount = records.stream()
            .filter(r -> "previous".equals(r.getRollbackType()))
            .count();
        assertEquals("Should have 1 specific rollback", 1, specificCount);
        assertEquals("Should have 1 previous rollback", 1, previousCount);
    }
}
