package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;

public class TaskInstanceDAOTest extends BaseElementTest {

    @Resource
    TaskInstanceDAO dao;

    TaskInstanceEntity entity = null;

    @Before
    public void before() {
        entity = new TaskInstanceEntity();
        entity.setProcessDefinitionId("processDefinitionId");
        entity.setActivityInstanceId(11L);
        entity.setClaimUserId("assign_id");
        Date claimTime = new Date();
        entity.setClaimTime(new Date(claimTime.getTime()));
        entity.setCompleteTime(new Date(claimTime.getTime() + 1000000));
        entity.setExecutionInstanceId(22L);
        entity.setPriority(333);
        entity.setStatus(TaskInstanceConstant.PENDING);
        entity.setProcessInstanceId(444L);
    }

    @Test
    public void testInsert() {

         dao.insert(entity);
        Assert.assertNotNull(entity);
    }

    @Test
    public void testFindOne() {
        dao.insert(entity);

        TaskInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);
    }

    @Test
    public void testDelete() {
        dao.insert(entity);

        TaskInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);

        // TODO 返回删除行数,去掉findAll 接口
        dao.delete(entity.getId());

        result = dao.findOne(entity.getId());
        Assert.assertNull(result);
    }

    @Test
    public void testUpdate() {
        dao.insert(entity);

        entity.setClaimUserId("assign_id_new");
        Date claimTime = new Date();

        entity.setClaimTime(new Date(claimTime.getTime()));
        Date endTime = new Date(claimTime.getTime() + 1000000);
        entity.setCompleteTime(endTime);
        entity.setPriority(333444);
        dao.update(entity);

        TaskInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);
        Assert.assertEquals("assign_id_new", entity.getClaimUserId());
        Assert.assertEquals(claimTime, entity.getClaimTime());
        Assert.assertEquals(endTime, entity.getCompleteTime());
        Assert.assertEquals(333444, entity.getPriority().intValue());

    }
}
