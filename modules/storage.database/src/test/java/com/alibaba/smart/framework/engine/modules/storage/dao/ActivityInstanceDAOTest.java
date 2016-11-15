package com.alibaba.smart.framework.engine.modules.storage.dao;

import com.alibaba.smart.framework.engine.modules.storage.entity.ActivityInstanceEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

public class ActivityInstanceDAOTest extends BaseTest {

    @Resource
    ActivityInstanceDAO dao;

    ActivityInstanceEntity entity = null;

    @Before
    public void before() {
        entity = new ActivityInstanceEntity();
        entity.setProcessDefinitionId("processDefinitionId");
        entity.setExecutionInstanceId(11L);
        entity.setProcessDefinitionActivityId("ProcessDefinitionActivityId");
        entity.setProcessDefinitionId("processDefinitionId");
        entity.setProcessInstanceId(22L);
        entity.setTaskInstanceId(33L);
    }

    @Test
    public void testInsert() {

        ActivityInstanceEntity result = dao.insert(entity);
        Assert.assertNotNull(result);
    }

    @Test
    public void testFindOne() {
        dao.insert(entity);

        ActivityInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);
    }

    @Test
    public void testDelete() {
        dao.insert(entity);

        ActivityInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);

        dao.delete(entity.getId());

        result = dao.findOne(entity.getId());
        Assert.assertNull(result);
    }

}
