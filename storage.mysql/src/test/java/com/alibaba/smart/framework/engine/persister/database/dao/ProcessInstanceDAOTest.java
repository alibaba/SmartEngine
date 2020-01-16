package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import com.alibaba.smart.framework.engine.persister.database.entity.ProcessInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProcessInstanceDAOTest extends BaseElementTest {

    @Resource
    ProcessInstanceDAO dao;

    ProcessInstanceEntity entity = null;

    @Before
    public void before() {
        entity = new ProcessInstanceEntity();
        entity.setProcessDefinitionIdAndVersion("processDefinitionId");
        entity.setStatus("running");
        Long id = System.currentTimeMillis();
        entity.setId(1L);
        entity.setBizUniqueId(String.valueOf(id * 1000 + new Random().nextInt(1000)) );
        entity.setTitle("title");
        entity.setStartUserId("234");
        entity.setTag("tag");
        entity.setComment("comment");
    }

    @Test
    public void testInsert() {

        dao.insert(entity);
        Assert.assertNotNull(entity);
    }



    @Test
    public void testFindOne() {
        dao.insert(entity);

        ProcessInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertEquals("title",result.getTitle());
        Assert.assertEquals("tag",result.getTag());
        Assert.assertNotNull(result);
    }

    @Test
    public void testFindByProcessInstanceIdList() {
        dao.insert(entity);
        ProcessInstanceQueryParam processInstanceQueryParam = new ProcessInstanceQueryParam();

        List<String>  processInstanceIdList = new ArrayList();
        processInstanceIdList.add(entity.getId().toString());

        processInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
        List<ProcessInstanceEntity> result = dao.find(processInstanceQueryParam);
        Assert.assertEquals("title",result.get(0).getTitle());
        Assert.assertEquals("tag",result.get(0).getTag());
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

        result = dao.findOneForUpdate(entity.getId());
        Assert.assertNull(result);
    }

    @Test
    public void testUpdate() {
        dao.insert(entity);
        ProcessInstanceEntity result = dao.findOne(entity.getId());

        Assert.assertEquals("comment",result.getComment());

        entity.setStatus("test_status");
        entity.setParentProcessInstanceId(222222L);
        entity.setTag("newTag");
        entity.setComment("new comment");
        dao.update(entity);

        result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);
        Assert.assertEquals("test_status", entity.getStatus());
        Assert.assertEquals(222222L, entity.getParentProcessInstanceId().longValue());
        Assert.assertEquals("newTag", entity.getTag());
        Assert.assertEquals("new comment", entity.getComment());

    }
}
