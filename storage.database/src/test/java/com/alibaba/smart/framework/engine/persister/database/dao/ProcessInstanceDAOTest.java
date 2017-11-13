package com.alibaba.smart.framework.engine.persister.database.dao;
import java.util.Date;
import java.util.Random;

import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

public class ProcessInstanceDAOTest extends BaseElementTest {

    @Resource
    ProcessInstanceDAO dao;

    ProcessInstanceEntity entity = null;

    @Before
    public void before() {
        entity = new ProcessInstanceEntity();
        entity.setProcessDefinitionIdAndVersion("processDefinitionId");
        entity.setStatus("running");
    }

    @Test
    public void testInsert() {

        dao.insert(entity);
        Assert.assertNotNull(entity);
    }

    @Test
    public void insertIgnore() {
        Long id = System.currentTimeMillis();
        id = id * 1000 + new Random().nextInt(1000);
        String user = "zaimang.tj";
        ProcessInstanceEntity entity = new ProcessInstanceEntity();
        entity.setProcessDefinitionIdAndVersion("1.0.0");
        entity.setStartUserId(user);
        entity.setParentProcessInstanceId(0L);
        entity.setStatus("test_status");
        entity.setProcessDefinitionType("112");
        entity.setBizUniqueId(String.valueOf(id));
        entity.setReason("111");
        entity.setId(id);
        entity.setGmtCreate(new Date());
        entity.setGmtModified(new Date());
        int count = dao.insertIgnore(entity);
        Assert.assertEquals(1, count);

        entity.setStartUserId("another_user");
        int count2 = dao.insertIgnore(entity);
        Assert.assertEquals(0, count2);
        ProcessInstanceEntity entityInDb = dao.findOne(id);
        Assert.assertEquals(user, entityInDb.getStartUserId());
    }

    @Test
    public void testFindOne() {
        dao.insert(entity);

        ProcessInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);
    }

    @Test
    public void testDelete() {
        dao.insert(entity);

        ProcessInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);

        // 返回删除行数,去掉findAll 接口
        dao.delete(entity.getId());

        result = dao.findOne(entity.getId());
        Assert.assertNull(result);
    }

    @Test
    public void testUpdate() {
        dao.insert(entity);

        entity.setStatus("test_status");
        entity.setParentProcessInstanceId(222222L);
        dao.update(entity);

        ProcessInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);
        Assert.assertEquals("test_status", entity.getStatus());
        Assert.assertEquals(222222L, entity.getParentProcessInstanceId().longValue());

    }
}
