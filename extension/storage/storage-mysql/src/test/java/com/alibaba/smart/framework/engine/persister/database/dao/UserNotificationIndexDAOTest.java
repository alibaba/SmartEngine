package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.Arrays;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.NotificationConstant;
import com.alibaba.smart.framework.engine.persister.database.entity.UserNotificationIndexEntity;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DAO tests for se_user_notification_index table.
 */
public class UserNotificationIndexDAOTest extends BaseElementTest {

    @Setter(onMethod = @__({@Autowired}))
    UserNotificationIndexDAO userNotificationIndexDAO;

    UserNotificationIndexEntity indexEntity = null;
    String tenantId = "sharding-test";

    @Before
    public void before() {
        indexEntity = new UserNotificationIndexEntity();
        indexEntity.setTenantId(tenantId);
        indexEntity.setReceiverUserId("receiver001");
        indexEntity.setNotificationId(5001L);
        indexEntity.setProcessInstanceId(2001L);
        indexEntity.setNotificationType(NotificationConstant.NotificationType.CC);
        indexEntity.setTitle("Test Notification");
        indexEntity.setReadStatus(NotificationConstant.ReadStatus.UNREAD);
        indexEntity.setGmtCreate(DateUtil.getCurrentDate());
        indexEntity.setGmtModified(DateUtil.getCurrentDate());
    }

    @Test
    public void testInsert() {
        userNotificationIndexDAO.insert(indexEntity);
        Assert.assertNotNull(indexEntity.getId());
        Assert.assertTrue(indexEntity.getId() > 0);
    }

    @Test
    public void testFindByReceiver() {
        userNotificationIndexDAO.insert(indexEntity);

        List<UserNotificationIndexEntity> results = userNotificationIndexDAO.findByReceiver(
                "receiver001", NotificationConstant.ReadStatus.UNREAD, tenantId, 0, 10);

        Assert.assertNotNull(results);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("receiver001", results.get(0).getReceiverUserId());
        Assert.assertEquals(NotificationConstant.ReadStatus.UNREAD, results.get(0).getReadStatus());
        Assert.assertEquals("Test Notification", results.get(0).getTitle());
    }

    @Test
    public void testCountByReceiver() {
        userNotificationIndexDAO.insert(indexEntity);

        // Insert second notification for same receiver
        UserNotificationIndexEntity entity2 = new UserNotificationIndexEntity();
        entity2.setTenantId(tenantId);
        entity2.setReceiverUserId("receiver001");
        entity2.setNotificationId(5002L);
        entity2.setProcessInstanceId(2002L);
        entity2.setNotificationType(NotificationConstant.NotificationType.INFORM);
        entity2.setTitle("Test Notification 2");
        entity2.setReadStatus(NotificationConstant.ReadStatus.UNREAD);
        entity2.setGmtCreate(DateUtil.getCurrentDate());
        entity2.setGmtModified(DateUtil.getCurrentDate());
        userNotificationIndexDAO.insert(entity2);

        Integer count = userNotificationIndexDAO.countByReceiver(
                "receiver001", NotificationConstant.ReadStatus.UNREAD, tenantId);
        Assert.assertEquals(Integer.valueOf(2), count);
    }

    @Test
    public void testUpdateReadStatus() {
        userNotificationIndexDAO.insert(indexEntity);

        userNotificationIndexDAO.updateReadStatus(
                5001L, NotificationConstant.ReadStatus.READ, tenantId);

        List<UserNotificationIndexEntity> unreadResults = userNotificationIndexDAO.findByReceiver(
                "receiver001", NotificationConstant.ReadStatus.UNREAD, tenantId, 0, 10);
        Assert.assertEquals(0, unreadResults.size());

        List<UserNotificationIndexEntity> readResults = userNotificationIndexDAO.findByReceiver(
                "receiver001", NotificationConstant.ReadStatus.READ, tenantId, 0, 10);
        Assert.assertEquals(1, readResults.size());
    }

    @Test
    public void testBatchUpdateReadStatus() {
        userNotificationIndexDAO.insert(indexEntity);

        UserNotificationIndexEntity entity2 = new UserNotificationIndexEntity();
        entity2.setTenantId(tenantId);
        entity2.setReceiverUserId("receiver001");
        entity2.setNotificationId(5002L);
        entity2.setProcessInstanceId(2002L);
        entity2.setNotificationType(NotificationConstant.NotificationType.INFORM);
        entity2.setReadStatus(NotificationConstant.ReadStatus.UNREAD);
        entity2.setGmtCreate(DateUtil.getCurrentDate());
        entity2.setGmtModified(DateUtil.getCurrentDate());
        userNotificationIndexDAO.insert(entity2);

        List<Long> notificationIds = Arrays.asList(5001L, 5002L);
        userNotificationIndexDAO.batchUpdateReadStatus(
                notificationIds, NotificationConstant.ReadStatus.READ, tenantId);

        Integer unreadCount = userNotificationIndexDAO.countByReceiver(
                "receiver001", NotificationConstant.ReadStatus.UNREAD, tenantId);
        Assert.assertEquals(Integer.valueOf(0), unreadCount);

        Integer readCount = userNotificationIndexDAO.countByReceiver(
                "receiver001", NotificationConstant.ReadStatus.READ, tenantId);
        Assert.assertEquals(Integer.valueOf(2), readCount);
    }

    @Test
    public void testDeleteByNotificationId() {
        userNotificationIndexDAO.insert(indexEntity);

        Integer countBefore = userNotificationIndexDAO.countByReceiver(
                "receiver001", NotificationConstant.ReadStatus.UNREAD, tenantId);
        Assert.assertEquals(Integer.valueOf(1), countBefore);

        userNotificationIndexDAO.deleteByNotificationId(5001L, tenantId);

        Integer countAfter = userNotificationIndexDAO.countByReceiver(
                "receiver001", NotificationConstant.ReadStatus.UNREAD, tenantId);
        Assert.assertEquals(Integer.valueOf(0), countAfter);
    }

    @Test
    public void testFindByReceiverWithPagination() {
        // Insert 5 notifications
        for (int i = 0; i < 5; i++) {
            UserNotificationIndexEntity entity = new UserNotificationIndexEntity();
            entity.setTenantId(tenantId);
            entity.setReceiverUserId("receiver001");
            entity.setNotificationId(6000L + i);
            entity.setProcessInstanceId(3000L + i);
            entity.setNotificationType(NotificationConstant.NotificationType.CC);
            entity.setReadStatus(NotificationConstant.ReadStatus.UNREAD);
            entity.setGmtCreate(DateUtil.getCurrentDate());
            entity.setGmtModified(DateUtil.getCurrentDate());
            userNotificationIndexDAO.insert(entity);
        }

        List<UserNotificationIndexEntity> page1 = userNotificationIndexDAO.findByReceiver(
                "receiver001", NotificationConstant.ReadStatus.UNREAD, tenantId, 0, 3);
        Assert.assertEquals(3, page1.size());

        List<UserNotificationIndexEntity> page2 = userNotificationIndexDAO.findByReceiver(
                "receiver001", NotificationConstant.ReadStatus.UNREAD, tenantId, 3, 3);
        Assert.assertEquals(2, page2.size());
    }

    @Test
    public void testFindByReceiverFiltersByReadStatus() {
        userNotificationIndexDAO.insert(indexEntity);

        // Read notifications should not appear in unread query
        userNotificationIndexDAO.updateReadStatus(
                5001L, NotificationConstant.ReadStatus.READ, tenantId);

        List<UserNotificationIndexEntity> unread = userNotificationIndexDAO.findByReceiver(
                "receiver001", NotificationConstant.ReadStatus.UNREAD, tenantId, 0, 10);
        Assert.assertEquals(0, unread.size());

        // But should appear in read query
        List<UserNotificationIndexEntity> read = userNotificationIndexDAO.findByReceiver(
                "receiver001", NotificationConstant.ReadStatus.READ, tenantId, 0, 10);
        Assert.assertEquals(1, read.size());
    }

    @Test
    public void testDifferentReceivers() {
        userNotificationIndexDAO.insert(indexEntity);

        UserNotificationIndexEntity entity2 = new UserNotificationIndexEntity();
        entity2.setTenantId(tenantId);
        entity2.setReceiverUserId("receiver002");
        entity2.setNotificationId(5010L);
        entity2.setProcessInstanceId(2010L);
        entity2.setNotificationType(NotificationConstant.NotificationType.INFORM);
        entity2.setReadStatus(NotificationConstant.ReadStatus.UNREAD);
        entity2.setGmtCreate(DateUtil.getCurrentDate());
        entity2.setGmtModified(DateUtil.getCurrentDate());
        userNotificationIndexDAO.insert(entity2);

        Integer count1 = userNotificationIndexDAO.countByReceiver(
                "receiver001", NotificationConstant.ReadStatus.UNREAD, tenantId);
        Integer count2 = userNotificationIndexDAO.countByReceiver(
                "receiver002", NotificationConstant.ReadStatus.UNREAD, tenantId);

        Assert.assertEquals(Integer.valueOf(1), count1);
        Assert.assertEquals(Integer.valueOf(1), count2);
    }
}
