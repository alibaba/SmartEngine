package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.persister.database.entity.ExecutionInstanceEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

public class ExecutionInstanceDAOTest extends BaseElementTest {

    @Resource
    ExecutionInstanceDAO dao;

    ExecutionInstanceEntity entity = null;

    @Before
    public void before() {
        entity = new ExecutionInstanceEntity();
        long id = System.currentTimeMillis();
        entity.setId(id);
        entity.setProcessDefinitionIdAndVersion("processDefinitionId");
        entity.setActivityInstanceId(11L);
        entity.setProcessInstanceId(444L);
        entity.setActive(false);
        entity.setProcessDefinitionActivityId("processDefinitionActivityId");
        entity.setProcessInstanceId(222L);
    }

    @Test
    public void testInsert() {

        dao.insert(entity);
        Assert.assertNotNull(entity);
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
