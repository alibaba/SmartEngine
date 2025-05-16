package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.persister.database.entity.ExecutionInstanceEntity;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ExecutionInstanceDAOTest extends BaseElementTest {

    @Setter(onMethod=@__({@Autowired}))
    ExecutionInstanceDAO dao;

    ExecutionInstanceEntity entity = null;

    @Before
    public void before() {
        entity = new ExecutionInstanceEntity();
        long id = System.currentTimeMillis();
        entity.setId(id);

        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());

        entity.setProcessDefinitionIdAndVersion("processDefinitionId");
        entity.setActivityInstanceId(11L);
        entity.setProcessInstanceId(444L);
        entity.setActive(false);
        entity.setProcessDefinitionActivityId("processDefinitionActivityId");
        entity.setProcessInstanceId(222L);

        entity.setBlockId(666L);
    }

    @Test
    public void testInsert() {

        dao.insert(entity);
        Assert.assertNotNull(entity);
    }

    @Test
    public void testFindOne() {
        dao.insert(entity);

        ExecutionInstanceEntity result = dao.findOne(entity.getId(),null);
        Assert.assertNotNull(result);
        Assert.assertEquals(666L,result.getBlockId().longValue());
    }

    @Test
    public void testDelete() {
        dao.insert(entity);

        ExecutionInstanceEntity result = dao.findOne(entity.getId(),null);
        Assert.assertNotNull(result);

        dao.delete(entity.getId(),null);

        result = dao.findOne(entity.getId(),null);
        Assert.assertNull(result);
    }

    @Test
    public void testUpdate() {
        dao.insert(entity);

        entity.setActive(true);

        dao.update(entity);

        ExecutionInstanceEntity result = dao.findOne(entity.getId(),null);
        Assert.assertNotNull(result);
        Assert.assertTrue(entity.isActive());

    }
}
