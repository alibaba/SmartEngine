package com.alibaba.smart.framework.engine.modules.storage.dao;

import com.alibaba.smart.framework.engine.modules.storage.entity.ExecutionInstanceEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

public class ExecutionInstanceDAOTest extends BaseTest {

    @Resource
    ExecutionInstanceDAO dao;

    ExecutionInstanceEntity entity = null;

    @Before
    public void before() {
        entity = new ExecutionInstanceEntity();
        entity.setProcessDefinitionId("processDefinitionId");
        entity.setActivityInstanceId(11L);
        entity.setProcessInstanceId(444L);
        entity.setActive(false);
        entity.setProcessDefinitionActivityId("processDefinitionActivityId");
        entity.setProcessInstanceId(222L);
    }

    @Test
    public void testInsert() {

        ExecutionInstanceEntity result = dao.insert(entity);
        Assert.assertNotNull(result);
    }

    @Test
    public void testFindOne() {
        dao.insert(entity);

        ExecutionInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);
    }

    @Test
    public void testDelete() {
        dao.insert(entity);

        ExecutionInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);

        dao.delete(entity.getId());

        result = dao.findOne(entity.getId());
        Assert.assertNull(result);
    }

    @Test
    public void testUpdate() {
        dao.insert(entity);

        entity.setActive(true);

        dao.update(entity);

        ExecutionInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);
        Assert.assertTrue(entity.isActive());

    }
}
