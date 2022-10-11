package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.persister.database.entity.ActivityInstanceEntity;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ActivityInstanceDAOTest extends BaseElementTest {

    @Setter(onMethod=@__({@Autowired}))
    ActivityInstanceDAO dao;

    ActivityInstanceEntity entity = null;

    @Before
    public void before() {
        entity = new ActivityInstanceEntity();
        entity.setId(1L);

        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());

        entity.setProcessDefinitionIdAndVersion("processDefinitionId");
//        entity.setExecutionInstanceId(11L);
        entity.setProcessDefinitionActivityId("ProcessDefinitionActivityId");
        //entity.setProcessDefinitionId("processDefinitionId");
        entity.setProcessInstanceId(22L);
//        entity.setTaskInstanceId(33L);
    }

    @Test
    public void testInsert() {

        dao.insert(entity);
        Assert.assertNotNull(entity);
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
