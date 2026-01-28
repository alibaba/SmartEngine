package com.alibaba.smart.framework.engine.test.dao;

import com.alibaba.smart.framework.engine.persister.database.dao.TaskTransferRecordDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskTransferRecordEntity;
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
 * TaskTransferRecordDAO单元测试
 *
 * @author SmartEngine Team
 */
@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TaskTransferRecordDAOTest extends DatabaseBaseTestCase {

    @Autowired
    private TaskTransferRecordDAO taskTransferRecordDAO;

    @Test
    public void testInsertAndSelect() {
        // 创建测试数据
        TaskTransferRecordEntity entity = new TaskTransferRecordEntity();
        entity.setGmtCreate(new Date());
        entity.setGmtModified(new Date());
        entity.setTaskInstanceId(12345L);
        entity.setFromUserId("user001");
        entity.setToUserId("user002");
        entity.setTransferReason("工作量过大需要协助");
        entity.setDeadline(new Date(System.currentTimeMillis() + 86400000)); // 1天后
        entity.setTenantId("tenant001");

        // 插入记录
        taskTransferRecordDAO.insert(entity);
        assertNotNull("ID should be auto-generated", entity.getId());

        // 查询记录
        TaskTransferRecordEntity retrieved = taskTransferRecordDAO.select(entity.getId(), "tenant001");
        assertNotNull("Retrieved entity should not be null", retrieved);
        assertEquals("Task instance ID should match", Long.valueOf(12345L), retrieved.getTaskInstanceId());
        assertEquals("From user ID should match", "user001", retrieved.getFromUserId());
        assertEquals("To user ID should match", "user002", retrieved.getToUserId());
        assertEquals("Transfer reason should match", "工作量过大需要协助", retrieved.getTransferReason());
        assertNotNull("Deadline should not be null", retrieved.getDeadline());
    }

    @Test
    public void testUpdate() {
        // 插入初始记录
        TaskTransferRecordEntity entity = new TaskTransferRecordEntity();
        entity.setGmtCreate(new Date());
        entity.setGmtModified(new Date());
        entity.setTaskInstanceId(12346L);
        entity.setFromUserId("user003");
        entity.setToUserId("user004");
        entity.setTransferReason("初始原因");
        entity.setTenantId("tenant001");
        taskTransferRecordDAO.insert(entity);

        // 更新记录
        entity.setTransferReason("更新后的移交原因");
        entity.setDeadline(new Date(System.currentTimeMillis() + 172800000)); // 2天后
        taskTransferRecordDAO.update(entity);

        // 验证更新
        TaskTransferRecordEntity retrieved = taskTransferRecordDAO.select(entity.getId(), "tenant001");
        assertEquals("Transfer reason should be updated", "更新后的移交原因", retrieved.getTransferReason());
        assertNotNull("Deadline should be set", retrieved.getDeadline());
    }

    @Test
    public void testSelectByTaskInstanceId() {
        Long taskInstanceId = 12347L;
        String tenantId = "tenant001";

        // 插入多条记录
        for (int i = 0; i < 3; i++) {
            TaskTransferRecordEntity entity = new TaskTransferRecordEntity();
            entity.setGmtCreate(new Date());
            entity.setGmtModified(new Date());
            entity.setTaskInstanceId(taskInstanceId);
            entity.setFromUserId("user00" + i);
            entity.setToUserId("user00" + (i + 1));
            entity.setTransferReason("移交原因 " + i);
            entity.setTenantId(tenantId);
            taskTransferRecordDAO.insert(entity);
        }

        // 查询任务的所有移交记录
        List<TaskTransferRecordEntity> records = taskTransferRecordDAO.selectByTaskInstanceId(taskInstanceId, tenantId);
        assertNotNull("Records should not be null", records);
        assertEquals("Should have 3 transfer records", 3, records.size());

        // 验证按创建时间倒序排列
        Date previousDate = records.get(0).getGmtCreate();
        for (int i = 1; i < records.size(); i++) {
            Date currentDate = records.get(i).getGmtCreate();
            assertTrue("Records should be ordered by gmt_create desc",
                previousDate.compareTo(currentDate) >= 0);
            previousDate = currentDate;
        }
    }

    @Test
    public void testDelete() {
        // 插入记录
        TaskTransferRecordEntity entity = new TaskTransferRecordEntity();
        entity.setGmtCreate(new Date());
        entity.setGmtModified(new Date());
        entity.setTaskInstanceId(12348L);
        entity.setFromUserId("user005");
        entity.setToUserId("user006");
        entity.setTransferReason("待删除的记录");
        entity.setTenantId("tenant001");
        taskTransferRecordDAO.insert(entity);

        Long recordId = entity.getId();
        assertNotNull("Record ID should exist", recordId);

        // 删除记录
        taskTransferRecordDAO.delete(recordId, "tenant001");

        // 验证已删除
        TaskTransferRecordEntity retrieved = taskTransferRecordDAO.select(recordId, "tenant001");
        assertNull("Record should be deleted", retrieved);
    }

    @Test
    public void testTenantIsolation() {
        // 插入不同租户的记录
        TaskTransferRecordEntity entity1 = new TaskTransferRecordEntity();
        entity1.setGmtCreate(new Date());
        entity1.setGmtModified(new Date());
        entity1.setTaskInstanceId(12349L);
        entity1.setFromUserId("user007");
        entity1.setToUserId("user008");
        entity1.setTransferReason("租户1的记录");
        entity1.setTenantId("tenant001");
        taskTransferRecordDAO.insert(entity1);

        TaskTransferRecordEntity entity2 = new TaskTransferRecordEntity();
        entity2.setGmtCreate(new Date());
        entity2.setGmtModified(new Date());
        entity2.setTaskInstanceId(12349L);
        entity2.setFromUserId("user009");
        entity2.setToUserId("user010");
        entity2.setTransferReason("租户2的记录");
        entity2.setTenantId("tenant002");
        taskTransferRecordDAO.insert(entity2);

        // 查询租户1的记录
        TaskTransferRecordEntity retrieved1 = taskTransferRecordDAO.select(entity1.getId(), "tenant001");
        assertNotNull("Tenant001 should see its record", retrieved1);

        // 租户1不应该看到租户2的记录
        TaskTransferRecordEntity retrieved2 = taskTransferRecordDAO.select(entity2.getId(), "tenant001");
        assertNull("Tenant001 should not see tenant002's record", retrieved2);

        // 租户2应该看到自己的记录
        TaskTransferRecordEntity retrieved3 = taskTransferRecordDAO.select(entity2.getId(), "tenant002");
        assertNotNull("Tenant002 should see its record", retrieved3);
    }
}
