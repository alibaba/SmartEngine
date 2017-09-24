package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.smart.framework.engine.persister.database.entity.DeploymentInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.DeploymentInstanceParam;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DeploymentInstanceDAOTest extends BaseElementTest {

    @Resource
    DeploymentInstanceDAO dao;

    DeploymentInstanceEntity entity = null;

    private static final String PROCESS_DEFINITION_CONTENT ="XML_CONTENT";

    @Before
    public void before() {
        entity = new DeploymentInstanceEntity();
        entity.setProcessDefinitionId("processDefinitionId");
        entity.setProcessDefinitionVersion("processDefinitionVersion");
        entity.setProcessDefinitionContent(PROCESS_DEFINITION_CONTENT);
        entity.setDeploymentUserId("userId");
        entity.setDeploymentStatus("deploymentStatus");
        entity.setLogicStatus("logicStatus");
    }

    @Test
    public void testInsert() {

       Object o =  dao.insert(entity);
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

        entity.setLogicStatus("updated");

        dao.update( entity);

        DeploymentInstanceEntity result = dao.findOne(entity.getId());
        Assert.assertNotNull(result);


        Assert.assertTrue("updated".equals(result.getLogicStatus()));
    }

    @Test
    public void testFindMany() {
        dao.insert(entity);

        entity = new DeploymentInstanceEntity();
        entity.setProcessDefinitionId("processDefinitionId1");
        entity.setProcessDefinitionVersion("processDefinitionVersion1");
        entity.setProcessDefinitionContent(PROCESS_DEFINITION_CONTENT);
        entity.setDeploymentUserId("userId");
        entity.setDeploymentStatus("deploymentStatus");
        entity.setLogicStatus("logicStatus");

        DeploymentInstanceParam deploymentInstanceParam = new DeploymentInstanceParam();
        deploymentInstanceParam.setDeploymentUserId("userId");

        dao.insert(entity);

        List<DeploymentInstanceEntity> result = dao.find(deploymentInstanceParam);
        Assert.assertNotNull(result);


        Assert.assertEquals(2, result.size() );
    }


}
