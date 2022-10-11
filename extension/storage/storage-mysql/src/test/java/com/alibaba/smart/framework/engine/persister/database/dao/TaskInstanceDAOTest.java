package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.Date;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;

import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskInstanceDAOTest extends BaseElementTest {

    @Setter(onMethod=@__({@Autowired}))
    TaskInstanceDAO dao;

    TaskInstanceEntity entity = null;

    @Before
    public void before() {
        entity = new TaskInstanceEntity();
        entity.setId(1L);

        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());

        entity.setProcessDefinitionIdAndVersion("processDefinitionId");
        entity.setActivityInstanceId(11L);
        entity.setClaimUserId("assign_id");
        Date claimTime = new Date();
        entity.setClaimTime(new Date(claimTime.getTime()));
        entity.setCompleteTime(new Date(claimTime.getTime() + 1000000));
        entity.setExecutionInstanceId(22L);
        entity.setPriority(333);
        entity.setStatus(TaskInstanceConstant.PENDING);
        entity.setProcessDefinitionActivityId("userTask");
        entity.setProcessInstanceId(444L);
        entity.setComment("comment");
        entity.setExtension("extension");
        entity.setTitle("title");
        entity.setTag("tag");
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
        Assert.assertNotNull(result.getClaimTime());
        Assert.assertEquals("comment",result.getComment());
        Assert.assertEquals("extension",result.getExtension());
    }

    @Test
    public void testDelete() {
        dao.insert(entity);

        TaskInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);

        dao.delete(entity.getId());

        result = dao.findOne(entity.getId());
        Assert.assertNull(result);
    }

    @Test
    public void testUpdate() {
        dao.insert(entity);

        TaskInstanceEntity result = dao.findOne(entity.getId());

        Assert.assertEquals("title", result.getTitle());


        entity.setClaimUserId("assign_id_new");
        Date claimTime = new Date();

        entity.setClaimTime(new Date(claimTime.getTime()));
        Date endTime = new Date(claimTime.getTime() + 1000000);
        entity.setCompleteTime(endTime);
        entity.setPriority(333444);
        entity.setComment("new_comment");
        entity.setExtension("new_extension");
        entity.setTitle("new_title");
        dao.update(entity);

        result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);
        Assert.assertEquals("assign_id_new", entity.getClaimUserId());
        Assert.assertEquals(claimTime, entity.getClaimTime());
        Assert.assertEquals(endTime, entity.getCompleteTime());
        Assert.assertEquals(333444, entity.getPriority().intValue());
        Assert.assertEquals("new_comment", result.getComment());
        Assert.assertEquals("new_extension", result.getExtension());
        Assert.assertEquals("new_title", result.getTitle());

    }

    @Test
    public void testQuery() {
        dao.insert(entity);
        TaskInstanceQueryParam param = new TaskInstanceQueryParam();
        param.setExtension("extension1");
        List<TaskInstanceEntity> result = dao.findTaskList(param);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() == 0);

        param = new TaskInstanceQueryParam();
        param.setExtension("extension");
        param.setTitle("title");
        param.setTag("tag");
        param.setComment("comment");
        param.setPriority(333);
        param.setClaimUserId("assign_id");

        result = dao.findTaskList(param);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() == 1);
        Assert.assertTrue(  result.get(0).getExtension().equals("extension"));
    }
}
