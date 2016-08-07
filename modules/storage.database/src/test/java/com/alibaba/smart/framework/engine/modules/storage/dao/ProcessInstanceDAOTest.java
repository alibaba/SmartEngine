package com.alibaba.smart.framework.engine.modules.storage.dao;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.smart.framework.engine.modules.storage.entity.ProcessInstanceEntity;

public class ProcessInstanceDAOTest extends BaseTest {

    @Resource
    ProcessInstanceDAO    dao;

    ProcessInstanceEntity entity = null;

    @Before
    public void before() {
        entity = new ProcessInstanceEntity();
        entity.setProcessDefinitionId("processDefinitionId");
        entity.setStatus("running");
    }

    @Test
    public void testInsert() {

        ProcessInstanceEntity result = dao.insert(entity);
        Assert.assertNotNull(result);
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