package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.NotificationConstant;
import com.alibaba.smart.framework.engine.persister.database.entity.NotificationInstanceEntity;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * 知会通知实例DAO测试
 * 
 * @author SmartEngine Team
 */
public class NotificationInstanceDAOTest extends BaseElementTest {

    @Setter(onMethod = @__({@Autowired}))
    NotificationInstanceDAO notificationInstanceDAO;

    NotificationInstanceEntity notificationInstance = null;
    String tenantId = "test-tenant";

    @Before
    public void before() {
        notificationInstance = new NotificationInstanceEntity();
        notificationInstance.setId(1L);
        notificationInstance.setGmtCreate(DateUtil.getCurrentDate());
        notificationInstance.setGmtModified(DateUtil.getCurrentDate());
        notificationInstance.setProcessInstanceId(100L);
        notificationInstance.setTaskInstanceId(200L);
        notificationInstance.setSenderUserId("sender001");
        notificationInstance.setReceiverUserId("receiver001");
        notificationInstance.setNotificationType(NotificationConstant.NotificationType.CC);
        notificationInstance.setTitle("测试通知标题");
        notificationInstance.setContent("测试通知内容");
        notificationInstance.setReadStatus(NotificationConstant.ReadStatus.UNREAD);
        notificationInstance.setTenantId(tenantId);
    }

    @Test
    public void testInsert() {
        notificationInstanceDAO.insert(notificationInstance);
        Assert.assertNotNull(notificationInstance.getId());
    }

    @Test
    public void testFindOne() {
        notificationInstanceDAO.insert(notificationInstance);
        
        NotificationInstanceEntity found = notificationInstanceDAO.findOne(notificationInstance.getId(), tenantId);
        Assert.assertNotNull(found);
        Assert.assertEquals(notificationInstance.getSenderUserId(), found.getSenderUserId());
        Assert.assertEquals(notificationInstance.getReceiverUserId(), found.getReceiverUserId());
        Assert.assertEquals(notificationInstance.getTitle(), found.getTitle());
        Assert.assertEquals(notificationInstance.getReadStatus(), found.getReadStatus());
    }

    @Test
    public void testFindByReceiver() {
        notificationInstanceDAO.insert(notificationInstance);
        
        List<NotificationInstanceEntity> notificationList = notificationInstanceDAO
                .findByReceiver("receiver001", NotificationConstant.ReadStatus.UNREAD, tenantId, 0, 10);
        
        Assert.assertNotNull(notificationList);
        Assert.assertTrue(notificationList.size() > 0);
        Assert.assertEquals("receiver001", notificationList.get(0).getReceiverUserId());
        Assert.assertEquals(NotificationConstant.ReadStatus.UNREAD, notificationList.get(0).getReadStatus());
    }

    @Test
    public void testFindBySender() {
        notificationInstanceDAO.insert(notificationInstance);
        
        List<NotificationInstanceEntity> notificationList = notificationInstanceDAO
                .findBySender("sender001", tenantId, 0, 10);
        
        Assert.assertNotNull(notificationList);
        Assert.assertTrue(notificationList.size() > 0);
        Assert.assertEquals("sender001", notificationList.get(0).getSenderUserId());
    }

    @Test
    public void testCountByReceiver() {
        notificationInstanceDAO.insert(notificationInstance);
        
        Integer count = notificationInstanceDAO.countByReceiver("receiver001", 
                NotificationConstant.ReadStatus.UNREAD, tenantId);
        Assert.assertNotNull(count);
        Assert.assertTrue(count > 0);
    }

    @Test
    public void testCountBySender() {
        notificationInstanceDAO.insert(notificationInstance);
        
        Integer count = notificationInstanceDAO.countBySender("sender001", tenantId);
        Assert.assertNotNull(count);
        Assert.assertTrue(count > 0);
    }

    @Test
    public void testMarkAsRead() {
        notificationInstanceDAO.insert(notificationInstance);
        
        int result = notificationInstanceDAO.markAsRead(notificationInstance.getId(), tenantId);
        Assert.assertEquals(1, result);
        
        NotificationInstanceEntity found = notificationInstanceDAO.findOne(notificationInstance.getId(), tenantId);
        Assert.assertEquals(NotificationConstant.ReadStatus.READ, found.getReadStatus());
        Assert.assertNotNull(found.getReadTime());
    }

    @Test
    public void testBatchMarkAsRead() {
        // 插入多个通知
        notificationInstanceDAO.insert(notificationInstance);
        
        NotificationInstanceEntity notification2 = new NotificationInstanceEntity();
        notification2.setId(2L);
        notification2.setGmtCreate(DateUtil.getCurrentDate());
        notification2.setGmtModified(DateUtil.getCurrentDate());
        notification2.setProcessInstanceId(101L);
        notification2.setTaskInstanceId(201L);
        notification2.setSenderUserId("sender001");
        notification2.setReceiverUserId("receiver001");
        notification2.setNotificationType(NotificationConstant.NotificationType.INFORM);
        notification2.setTitle("测试通知标题2");
        notification2.setContent("测试通知内容2");
        notification2.setReadStatus(NotificationConstant.ReadStatus.UNREAD);
        notification2.setTenantId(tenantId);
        notificationInstanceDAO.insert(notification2);
        
        List<Long> ids = Arrays.asList(notificationInstance.getId(), notification2.getId());
        int result = notificationInstanceDAO.batchMarkAsRead(ids, tenantId);
        Assert.assertEquals(2, result);
        
        NotificationInstanceEntity found1 = notificationInstanceDAO.findOne(notificationInstance.getId(), tenantId);
        NotificationInstanceEntity found2 = notificationInstanceDAO.findOne(notification2.getId(), tenantId);
        Assert.assertEquals(NotificationConstant.ReadStatus.READ, found1.getReadStatus());
        Assert.assertEquals(NotificationConstant.ReadStatus.READ, found2.getReadStatus());
    }

    @Test
    public void testUpdate() {
        notificationInstanceDAO.insert(notificationInstance);
        
        notificationInstance.setTitle("更新后的标题");
        notificationInstance.setContent("更新后的内容");
        notificationInstanceDAO.update(notificationInstance);
        
        NotificationInstanceEntity found = notificationInstanceDAO.findOne(notificationInstance.getId(), tenantId);
        Assert.assertEquals("更新后的标题", found.getTitle());
        Assert.assertEquals("更新后的内容", found.getContent());
    }

    @Test
    public void testDelete() {
        notificationInstanceDAO.insert(notificationInstance);
        Long id = notificationInstance.getId();
        
        notificationInstanceDAO.delete(id, tenantId);
        
        NotificationInstanceEntity found = notificationInstanceDAO.findOne(id, tenantId);
        Assert.assertNull(found);
    }
}