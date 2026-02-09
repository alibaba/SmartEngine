package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.constant.NotificationConstant;
import com.alibaba.smart.framework.engine.persister.database.entity.NotificationInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.UserNotificationIndexEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.UserTaskIndexEntity;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests verifying sharding index table maintenance workflows.
 * Simulates the Storage layer's index maintenance logic at DAO level.
 */
public class ShardingIndexMaintenanceTest extends BaseElementTest {

    @Setter(onMethod = @__({@Autowired}))
    TaskInstanceDAO taskDao;

    @Setter(onMethod = @__({@Autowired}))
    TaskAssigneeDAO taskAssigneeDAO;

    @Setter(onMethod = @__({@Autowired}))
    UserTaskIndexDAO userTaskIndexDAO;

    @Setter(onMethod = @__({@Autowired}))
    NotificationInstanceDAO notificationInstanceDAO;

    @Setter(onMethod = @__({@Autowired}))
    UserNotificationIndexDAO userNotificationIndexDAO;

    String tenantId = "sharding-integ-test";

    TaskInstanceEntity task;
    TaskAssigneeEntity assignee1;
    TaskAssigneeEntity assignee2;

    @Before
    public void before() {
        // Task instance
        task = new TaskInstanceEntity();
        task.setId(8001L);
        task.setGmtCreate(DateUtil.getCurrentDate());
        task.setGmtModified(DateUtil.getCurrentDate());
        task.setProcessInstanceId(9001L);
        task.setProcessDefinitionIdAndVersion("approval:1.0");
        task.setProcessDefinitionType("approval");
        task.setActivityInstanceId(7001L);
        task.setProcessDefinitionActivityId("userTask1");
        task.setExecutionInstanceId(6001L);
        task.setStatus(TaskInstanceConstant.PENDING);
        task.setTitle("Approval Request");
        task.setPriority(500);
        task.setDomainCode("HR");
        task.setTenantId(tenantId);

        // Assignee 1
        assignee1 = new TaskAssigneeEntity();
        assignee1.setId(8101L);
        assignee1.setGmtCreate(DateUtil.getCurrentDate());
        assignee1.setGmtModified(DateUtil.getCurrentDate());
        assignee1.setProcessInstanceId(9001L);
        assignee1.setTaskInstanceId(8001L);
        assignee1.setAssigneeId("approver001");
        assignee1.setAssigneeType("user");
        assignee1.setTenantId(tenantId);

        // Assignee 2
        assignee2 = new TaskAssigneeEntity();
        assignee2.setId(8102L);
        assignee2.setGmtCreate(DateUtil.getCurrentDate());
        assignee2.setGmtModified(DateUtil.getCurrentDate());
        assignee2.setProcessInstanceId(9001L);
        assignee2.setTaskInstanceId(8001L);
        assignee2.setAssigneeId("approver002");
        assignee2.setAssigneeType("user");
        assignee2.setTenantId(tenantId);
    }

    // =============================================
    // Test: Task assignee insert → index table populated
    // =============================================

    @Test
    public void testAssigneeInsert_shouldPopulateIndex() {
        // Step 1: Insert task
        taskDao.insert(task);

        // Step 2: Insert assignee (simulating what Storage layer does)
        taskAssigneeDAO.insert(assignee1);

        // Step 3: Build index entry (simulating Storage layer's sharding logic)
        UserTaskIndexEntity indexEntity = buildUserTaskIndex(task, assignee1);
        userTaskIndexDAO.insert(indexEntity);

        // Verify: Index table should have the entry
        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(tenantId);
        param.setAssigneeUserId("approver001");

        List<UserTaskIndexEntity> results = userTaskIndexDAO.findByAssignee(param);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("Approval Request", results.get(0).getTitle());
        Assert.assertEquals("approval", results.get(0).getProcessDefinitionType());
        Assert.assertEquals("HR", results.get(0).getDomainCode());
        Assert.assertEquals(Long.valueOf(9001L), results.get(0).getProcessInstanceId());
    }

    @Test
    public void testMultipleAssignees_shouldCreateMultipleIndexEntries() {
        taskDao.insert(task);
        taskAssigneeDAO.insert(assignee1);
        taskAssigneeDAO.insert(assignee2);

        userTaskIndexDAO.insert(buildUserTaskIndex(task, assignee1));
        userTaskIndexDAO.insert(buildUserTaskIndex(task, assignee2));

        // Both assignees should see the task
        TaskInstanceQueryByAssigneeParam param1 = new TaskInstanceQueryByAssigneeParam();
        param1.setTenantId(tenantId);
        param1.setAssigneeUserId("approver001");
        Assert.assertEquals(Integer.valueOf(1), userTaskIndexDAO.countByAssignee(param1));

        TaskInstanceQueryByAssigneeParam param2 = new TaskInstanceQueryByAssigneeParam();
        param2.setTenantId(tenantId);
        param2.setAssigneeUserId("approver002");
        Assert.assertEquals(Integer.valueOf(1), userTaskIndexDAO.countByAssignee(param2));
    }

    // =============================================
    // Test: Task complete → index entries removed
    // =============================================

    @Test
    public void testTaskComplete_shouldRemoveIndexEntries() {
        // Setup: task + assignees + index entries
        taskDao.insert(task);
        taskAssigneeDAO.insert(assignee1);
        taskAssigneeDAO.insert(assignee2);
        userTaskIndexDAO.insert(buildUserTaskIndex(task, assignee1));
        userTaskIndexDAO.insert(buildUserTaskIndex(task, assignee2));

        // Verify index entries exist
        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(tenantId);
        param.setAssigneeUserId("approver001");
        Assert.assertEquals(Integer.valueOf(1), userTaskIndexDAO.countByAssignee(param));

        // Complete the task (update status)
        task.setStatus(TaskInstanceConstant.COMPLETED);
        task.setGmtModified(DateUtil.getCurrentDate());
        taskDao.update(task);

        // Simulating Storage layer: delete index entries on terminal status
        userTaskIndexDAO.deleteByTaskInstanceId(8001L, tenantId);

        // Verify: All index entries for this task should be gone
        param.setAssigneeUserId("approver001");
        Assert.assertEquals(Integer.valueOf(0), userTaskIndexDAO.countByAssignee(param));

        param.setAssigneeUserId("approver002");
        Assert.assertEquals(Integer.valueOf(0), userTaskIndexDAO.countByAssignee(param));
    }

    // =============================================
    // Test: Assignee remove → only that index entry removed
    // =============================================

    @Test
    public void testAssigneeRemove_shouldRemoveOnlyThatIndexEntry() {
        taskDao.insert(task);
        taskAssigneeDAO.insert(assignee1);
        taskAssigneeDAO.insert(assignee2);
        userTaskIndexDAO.insert(buildUserTaskIndex(task, assignee1));
        userTaskIndexDAO.insert(buildUserTaskIndex(task, assignee2));

        // Remove assignee1
        taskAssigneeDAO.delete(assignee1.getId(), tenantId);
        userTaskIndexDAO.deleteByAssigneeAndTask("approver001", 8001L, tenantId);

        // approver001 should have no tasks
        TaskInstanceQueryByAssigneeParam param1 = new TaskInstanceQueryByAssigneeParam();
        param1.setTenantId(tenantId);
        param1.setAssigneeUserId("approver001");
        Assert.assertEquals(Integer.valueOf(0), userTaskIndexDAO.countByAssignee(param1));

        // approver002 should still have the task
        TaskInstanceQueryByAssigneeParam param2 = new TaskInstanceQueryByAssigneeParam();
        param2.setTenantId(tenantId);
        param2.setAssigneeUserId("approver002");
        Assert.assertEquals(Integer.valueOf(1), userTaskIndexDAO.countByAssignee(param2));
    }

    // =============================================
    // Test: Notification index maintenance
    // =============================================

    @Test
    public void testNotificationInsert_shouldPopulateIndex() {
        NotificationInstanceEntity notification = new NotificationInstanceEntity();
        notification.setId(9501L);
        notification.setGmtCreate(DateUtil.getCurrentDate());
        notification.setGmtModified(DateUtil.getCurrentDate());
        notification.setProcessInstanceId(9001L);
        notification.setTaskInstanceId(8001L);
        notification.setSenderUserId("sender001");
        notification.setReceiverUserId("receiver001");
        notification.setNotificationType(NotificationConstant.NotificationType.CC);
        notification.setTitle("CC Notification");
        notification.setReadStatus(NotificationConstant.ReadStatus.UNREAD);
        notification.setTenantId(tenantId);

        notificationInstanceDAO.insert(notification);

        // Build index entry (simulating Storage layer)
        UserNotificationIndexEntity indexEntity = new UserNotificationIndexEntity();
        indexEntity.setTenantId(tenantId);
        indexEntity.setReceiverUserId("receiver001");
        indexEntity.setNotificationId(9501L);
        indexEntity.setProcessInstanceId(9001L);
        indexEntity.setNotificationType(NotificationConstant.NotificationType.CC);
        indexEntity.setTitle("CC Notification");
        indexEntity.setReadStatus(NotificationConstant.ReadStatus.UNREAD);
        indexEntity.setGmtCreate(DateUtil.getCurrentDate());
        indexEntity.setGmtModified(DateUtil.getCurrentDate());
        userNotificationIndexDAO.insert(indexEntity);

        // Verify
        Integer count = userNotificationIndexDAO.countByReceiver(
                "receiver001", NotificationConstant.ReadStatus.UNREAD, tenantId);
        Assert.assertEquals(Integer.valueOf(1), count);
    }

    @Test
    public void testNotificationMarkAsRead_shouldUpdateIndex() {
        NotificationInstanceEntity notification = new NotificationInstanceEntity();
        notification.setId(9502L);
        notification.setGmtCreate(DateUtil.getCurrentDate());
        notification.setGmtModified(DateUtil.getCurrentDate());
        notification.setProcessInstanceId(9001L);
        notification.setSenderUserId("sender001");
        notification.setReceiverUserId("receiver001");
        notification.setNotificationType(NotificationConstant.NotificationType.INFORM);
        notification.setTitle("Inform Notification");
        notification.setReadStatus(NotificationConstant.ReadStatus.UNREAD);
        notification.setTenantId(tenantId);

        notificationInstanceDAO.insert(notification);

        UserNotificationIndexEntity indexEntity = new UserNotificationIndexEntity();
        indexEntity.setTenantId(tenantId);
        indexEntity.setReceiverUserId("receiver001");
        indexEntity.setNotificationId(9502L);
        indexEntity.setProcessInstanceId(9001L);
        indexEntity.setNotificationType(NotificationConstant.NotificationType.INFORM);
        indexEntity.setTitle("Inform Notification");
        indexEntity.setReadStatus(NotificationConstant.ReadStatus.UNREAD);
        indexEntity.setGmtCreate(DateUtil.getCurrentDate());
        indexEntity.setGmtModified(DateUtil.getCurrentDate());
        userNotificationIndexDAO.insert(indexEntity);

        // Mark as read in main table
        notificationInstanceDAO.markAsRead(9502L, tenantId);

        // Sync to index table
        userNotificationIndexDAO.updateReadStatus(
                9502L, NotificationConstant.ReadStatus.READ, tenantId);

        // Verify: unread count should be 0
        Integer unreadCount = userNotificationIndexDAO.countByReceiver(
                "receiver001", NotificationConstant.ReadStatus.UNREAD, tenantId);
        Assert.assertEquals(Integer.valueOf(0), unreadCount);
    }

    // =============================================
    // Test: Query from index table returns correct data
    // =============================================

    @Test
    public void testQueryFromIndex_matchesMainTableData() {
        taskDao.insert(task);
        taskAssigneeDAO.insert(assignee1);
        userTaskIndexDAO.insert(buildUserTaskIndex(task, assignee1));

        // Query from index
        TaskInstanceQueryByAssigneeParam param = new TaskInstanceQueryByAssigneeParam();
        param.setTenantId(tenantId);
        param.setAssigneeUserId("approver001");
        param.setStatus(TaskInstanceConstant.PENDING);
        param.setProcessDefinitionType("approval");

        List<UserTaskIndexEntity> indexResults = userTaskIndexDAO.findByAssignee(param);
        Assert.assertEquals(1, indexResults.size());

        UserTaskIndexEntity indexRow = indexResults.get(0);

        // Query from main table
        TaskInstanceEntity mainRow = taskDao.findOne(8001L, tenantId);

        // Verify data consistency
        Assert.assertEquals(mainRow.getProcessInstanceId(), indexRow.getProcessInstanceId());
        Assert.assertEquals(mainRow.getTitle(), indexRow.getTitle());
        Assert.assertEquals(mainRow.getPriority(), indexRow.getPriority());
        Assert.assertEquals(mainRow.getProcessDefinitionType(), indexRow.getProcessDefinitionType());
        Assert.assertEquals(mainRow.getDomainCode(), indexRow.getDomainCode());
    }

    // =============================================
    // Helper methods
    // =============================================

    private UserTaskIndexEntity buildUserTaskIndex(TaskInstanceEntity task, TaskAssigneeEntity assignee) {
        UserTaskIndexEntity entity = new UserTaskIndexEntity();
        entity.setTenantId(task.getTenantId());
        entity.setAssigneeId(assignee.getAssigneeId());
        entity.setAssigneeType(assignee.getAssigneeType());
        entity.setTaskInstanceId(task.getId());
        entity.setProcessInstanceId(task.getProcessInstanceId());
        entity.setProcessDefinitionType(task.getProcessDefinitionType());
        entity.setDomainCode(task.getDomainCode());
        entity.setExtra(task.getExtra());
        entity.setTaskStatus(task.getStatus());
        entity.setTaskGmtModified(task.getGmtModified());
        entity.setTitle(task.getTitle());
        entity.setPriority(task.getPriority());
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        return entity;
    }
}
