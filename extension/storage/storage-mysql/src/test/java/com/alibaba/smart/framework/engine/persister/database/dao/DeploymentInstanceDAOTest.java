package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.LogicStatusConstant;
import com.alibaba.smart.framework.engine.persister.database.entity.DeploymentInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.DeploymentInstanceQueryParam;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DeploymentInstanceDAOTest extends BaseElementTest {

    @Setter(onMethod=@__({@Autowired}))
    DeploymentInstanceDAO dao;

    DeploymentInstanceEntity entity = null;

    private static final String PROCESS_DEFINITION_CONTENT ="XML_CONTENT";

    @Before
    public void before() {
        entity = new DeploymentInstanceEntity();
        entity.setId(1L);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setProcessDefinitionId("processDefinitionId");
        entity.setProcessDefinitionVersion("processDefinitionVersion");
        entity.setProcessDefinitionContent(PROCESS_DEFINITION_CONTENT);
        entity.setDeploymentUserId("userId");
        entity.setDeploymentStatus("deploymentStatus");
        entity.setProcessDefinitionName("TestName");
        entity.setProcessDefinitionDesc("Hello world");
        entity.setLogicStatus("logicStatus");
    }

    @Test
    public void testInsert() {

        dao.insert(entity);
        Assert.assertNotNull(entity.getId());
    }

    @Test
    public void testFindOne() {
        dao.insert(entity);

        Long id = entity.getId();
        DeploymentInstanceEntity result = dao.findOne(id);
        Assert.assertNotNull(result);
    }

    @Test
    public void testDelete() {
        dao.insert(entity);

        DeploymentInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);

        dao.delete(entity.getId());

        result = dao.findOne(entity.getId());
        Assert.assertNull(result);
    }

    @Test
    public void testUpdate() {
        dao.insert(entity);

        entity.setProcessDefinitionContent("abc_content");

        dao.update( entity);

        DeploymentInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);


        Assert.assertTrue("abc_content".equals(result.getProcessDefinitionContent()));
    }

    @Test
    public void testFindMany() {
        dao.insert(entity);
        DeploymentInstanceQueryParam deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setProcessDefinitionDescLike("world");
        List<DeploymentInstanceEntity> result = dao.findByPage(deploymentInstanceQueryParam);
        Assert.assertEquals(1, result.size());

        entity = new DeploymentInstanceEntity();
        long id = System.currentTimeMillis();
        entity.setId(id);
        entity.setGmtCreate(DateUtil.getCurrentDate());
        entity.setGmtModified(DateUtil.getCurrentDate());
        entity.setProcessDefinitionId("processDefinitionId1");
        entity.setProcessDefinitionVersion("processDefinitionVersion1");
        entity.setProcessDefinitionName("TestName");
        entity.setProcessDefinitionContent(PROCESS_DEFINITION_CONTENT);
        entity.setDeploymentUserId("userId");
        entity.setDeploymentStatus("deploymentStatus");
        entity.setLogicStatus(LogicStatusConstant.VALID);
        deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setDeploymentUserId("userId");

        dao.insert(entity);

        result = dao.findByPage(deploymentInstanceQueryParam);
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size() );

        deploymentInstanceQueryParam = new DeploymentInstanceQueryParam();
        deploymentInstanceQueryParam.setProcessDefinitionNameLike("Test");
        result = dao.findByPage(deploymentInstanceQueryParam);
        Assert.assertEquals(2, result.size());
    }


}
