package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.SupervisionConstant;
import com.alibaba.smart.framework.engine.persister.database.entity.SupervisionInstanceEntity;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 督办实例DAO测试
 * 
 * @author SmartEngine Team
 */
public class SupervisionInstanceDAOTest extends BaseElementTest {

    @Setter(onMethod = @__({@Autowired}))
    SupervisionInstanceDAO supervisionInstanceDAO;

    SupervisionInstanceEntity supervisionInstance = null;
    String tenantId = "test-tenant";

    @Before
    public void before() {
        supervisionInstance = new SupervisionInstanceEntity();
        supervisionInstance.setId(1L);
        supervisionInstance.setGmtCreate(DateUtil.getCurrentDate());
        supervisionInstance.setGmtModified(DateUtil.getCurrentDate());
        supervisionInstance.setProcessInstanceId(100L);
        supervisionInstance.setTaskInstanceId(200L);
        supervisionInstance.setSupervisorUserId("supervisor001");
        supervisionInstance.setSupervisionReason("测试督办原因");
        supervisionInstance.setSupervisionType(SupervisionConstant.SupervisionType.URGE);
        supervisionInstance.setStatus(SupervisionConstant.SupervisionStatus.ACTIVE);
        supervisionInstance.setTenantId(tenantId);
    }

    @Test
    public void testInsert() {
        supervisionInstanceDAO.insert(supervisionInstance);
        Assert.assertNotNull(supervisionInstance.getId());
    }

    @Test
    public void testFindOne() {
        supervisionInstanceDAO.insert(supervisionInstance);
        
        SupervisionInstanceEntity found = supervisionInstanceDAO.findOne(supervisionInstance.getId(), tenantId);
        Assert.assertNotNull(found);
        Assert.assertEquals(supervisionInstance.getSupervisorUserId(), found.getSupervisorUserId());
        Assert.assertEquals(supervisionInstance.getSupervisionType(), found.getSupervisionType());
        Assert.assertEquals(supervisionInstance.getStatus(), found.getStatus());
    }

    @Test
    public void testFindBySupervisor() {
        supervisionInstanceDAO.insert(supervisionInstance);
        
        List<SupervisionInstanceEntity> supervisionList = supervisionInstanceDAO
                .findBySupervisor("supervisor001", tenantId, 0, 10);
        
        Assert.assertNotNull(supervisionList);
        Assert.assertTrue(supervisionList.size() > 0);
        Assert.assertEquals("supervisor001", supervisionList.get(0).getSupervisorUserId());
    }

    @Test
    public void testFindActiveByTaskId() {
        supervisionInstanceDAO.insert(supervisionInstance);
        
        List<SupervisionInstanceEntity> supervisionList = supervisionInstanceDAO
                .findActiveByTaskId(200L, tenantId);
        
        Assert.assertNotNull(supervisionList);
        Assert.assertTrue(supervisionList.size() > 0);
        Assert.assertEquals(SupervisionConstant.SupervisionStatus.ACTIVE, supervisionList.get(0).getStatus());
    }

    @Test
    public void testCountBySupervisor() {
        supervisionInstanceDAO.insert(supervisionInstance);
        
        Integer count = supervisionInstanceDAO.countBySupervisor("supervisor001", tenantId);
        Assert.assertNotNull(count);
        Assert.assertTrue(count > 0);
    }

    @Test
    public void testCloseSupervision() {
        supervisionInstanceDAO.insert(supervisionInstance);
        
        int result = supervisionInstanceDAO.closeSupervision(supervisionInstance.getId(), tenantId);
        Assert.assertEquals(1, result);
        
        SupervisionInstanceEntity found = supervisionInstanceDAO.findOne(supervisionInstance.getId(), tenantId);
        Assert.assertEquals(SupervisionConstant.SupervisionStatus.CLOSED, found.getStatus());
    }

    @Test
    public void testCloseByTaskId() {
        supervisionInstanceDAO.insert(supervisionInstance);
        
        int result = supervisionInstanceDAO.closeByTaskId(200L, tenantId);
        Assert.assertEquals(1, result);
        
        SupervisionInstanceEntity found = supervisionInstanceDAO.findOne(supervisionInstance.getId(), tenantId);
        Assert.assertEquals(SupervisionConstant.SupervisionStatus.CLOSED, found.getStatus());
    }

    @Test
    public void testUpdate() {
        supervisionInstanceDAO.insert(supervisionInstance);
        
        supervisionInstance.setSupervisionReason("更新后的督办原因");
        supervisionInstanceDAO.update(supervisionInstance);
        
        SupervisionInstanceEntity found = supervisionInstanceDAO.findOne(supervisionInstance.getId(), tenantId);
        Assert.assertEquals("更新后的督办原因", found.getSupervisionReason());
    }

    @Test
    public void testDelete() {
        supervisionInstanceDAO.insert(supervisionInstance);
        Long id = supervisionInstance.getId();
        
        supervisionInstanceDAO.delete(id, tenantId);
        
        SupervisionInstanceEntity found = supervisionInstanceDAO.findOne(id, tenantId);
        Assert.assertNull(found);
    }
}