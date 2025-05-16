package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryParam;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TaskInstanceDAOTest extends BaseElementTest {

    @Setter(onMethod = @__({@Autowired}))
    TaskInstanceDAO taskDao;

    @Setter(onMethod = @__({@Autowired}))
    TaskAssigneeDAO taskAssigneeDAO;

    TaskInstanceEntity task = null;

    String tenantId = "-3";

    TaskAssigneeEntity taskAssignee4 = null;
    TaskAssigneeEntity taskAssignee5 = null;
    TaskAssigneeEntity taskAssignee6 = null;

    @Before
    public void before() {
        task = new TaskInstanceEntity();
        task.setId(1L);

        task.setGmtCreate(DateUtil.getCurrentDate());
        task.setGmtModified(DateUtil.getCurrentDate());

        task.setProcessDefinitionIdAndVersion("processDefinitionId");
        task.setActivityInstanceId(11L);
        task.setClaimUserId("assign_id");
        Date claimTime = new Date();
        task.setClaimTime(new Date(claimTime.getTime()));
        task.setCompleteTime(new Date(claimTime.getTime() + 1000000));
        task.setExecutionInstanceId(22L);
        task.setPriority(333);
        task.setStatus(TaskInstanceConstant.PENDING);
        task.setProcessDefinitionActivityId("userTask");
        task.setProcessInstanceId(444L);
        task.setComment("comment");
        task.setExtension("extension");
        task.setTitle("title");
        task.setTag("tag");
        task.setTenantId(tenantId);


        taskAssignee4 = new TaskAssigneeEntity();

        taskAssignee4.setGmtCreate(DateUtil.getCurrentDate());
        taskAssignee4.setGmtModified(DateUtil.getCurrentDate());

        taskAssignee4.setAssigneeId("123");
        taskAssignee4.setAssigneeType("user");
        taskAssignee4.setId(2004L);
        taskAssignee4.setProcessInstanceId(444L);
        taskAssignee4.setTaskInstanceId(1L);
        taskAssignee4.setTenantId(tenantId);

        taskAssignee5 = new TaskAssigneeEntity();

        taskAssignee5.setGmtCreate(DateUtil.getCurrentDate());
        taskAssignee5.setGmtModified(DateUtil.getCurrentDate());

        taskAssignee5.setAssigneeId("123");
        taskAssignee5.setAssigneeType("user");
        taskAssignee5.setId(2005L);
        taskAssignee5.setProcessInstanceId(444L);
        taskAssignee5.setTaskInstanceId(1L);
        taskAssignee5.setTenantId(tenantId);


        taskAssignee6 = new TaskAssigneeEntity();

        taskAssignee6.setGmtCreate(DateUtil.getCurrentDate());
        taskAssignee6.setGmtModified(DateUtil.getCurrentDate());

        taskAssignee6.setAssigneeId("123");
        taskAssignee6.setAssigneeType("user");
        taskAssignee6.setId(2006L);
        taskAssignee6.setProcessInstanceId(444L);
        taskAssignee6.setTaskInstanceId(1L);
        taskAssignee6.setTenantId(tenantId);
    }

    @Test
    public void testInsert() {

        taskDao.insert(task);
        Assert.assertNotNull(task);
        Assert.assertEquals(task.getTenantId(), tenantId);
    }

    @Test
    public void testFindOne() {
        taskDao.insert(task);

        TaskInstanceEntity result = taskDao.findOne(task.getId(), tenantId);
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getClaimTime());
        Assert.assertEquals("comment", result.getComment());
        Assert.assertEquals("extension", result.getExtension());
    }

    @Test
    public void testDelete() {
        taskDao.insert(task);

        TaskInstanceEntity result = taskDao.findOne(task.getId(), tenantId);
        Assert.assertNotNull(result);

        taskDao.delete(task.getId(), tenantId);

        result = taskDao.findOne(task.getId(), tenantId);
        Assert.assertNull(result);
    }

    @Test
    public void testUpdate() {
        taskDao.insert(task);

        TaskInstanceEntity result = taskDao.findOne(task.getId(), tenantId);

        Assert.assertEquals("title", result.getTitle());


        task.setClaimUserId("assign_id_new");
        Date claimTime = new Date();

        task.setClaimTime(new Date(claimTime.getTime()));
        Date endTime = new Date(claimTime.getTime() + 1000000);
        task.setCompleteTime(endTime);
        task.setPriority(333444);
        task.setComment("new_comment");
        task.setExtension("new_extension");
        task.setTitle("new_title");
        task.setTenantId(tenantId);

        taskDao.update(task);

        result = taskDao.findOne(task.getId(), tenantId);
        Assert.assertNotNull(result);
        Assert.assertEquals("assign_id_new", task.getClaimUserId());
        Assert.assertEquals(claimTime, task.getClaimTime());
        Assert.assertEquals(endTime, task.getCompleteTime());
        Assert.assertEquals(333444, task.getPriority().intValue());
        Assert.assertEquals("new_comment", result.getComment());
        Assert.assertEquals("new_extension", result.getExtension());
        Assert.assertEquals("new_title", result.getTitle());

    }

    @Test
    public void testQuery() {
        taskDao.insert(task);
        TaskInstanceQueryParam param = new TaskInstanceQueryParam();
        param.setExtension("extension1");
        param.setTenantId(tenantId);
        List<TaskInstanceEntity> result = taskDao.findTaskList(param);

        Assert.assertNotNull(result);
        Assert.assertEquals(0, result.size());

        param = new TaskInstanceQueryParam();
        param.setExtension("extension");
        param.setTitle("title");
        param.setTag("tag");
        param.setComment("comment");
        param.setPriority(333);
        param.setClaimUserId("assign_id");
        param.setTenantId(tenantId);

        result = taskDao.findTaskList(param);

        Assert.assertNotNull(result);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("extension", result.get(0).getExtension());
    }

    @Test
    public void findTaskByAssigneeTest() {
        taskAssigneeDAO.insert(taskAssignee4);
        taskAssigneeDAO.insert(taskAssignee5);
        taskAssigneeDAO.insert(taskAssignee6);
        taskDao.insert(task);

        List<TaskAssigneeEntity> taskAssigneeEntityList = taskAssigneeDAO.findListForInstanceList(Collections.singletonList(1L), tenantId);
        Assert.assertEquals(3, taskAssigneeEntityList.size());

        TaskInstanceEntity taskInstance = taskDao.findOne(1L, tenantId);

        Assert.assertNotNull(taskAssigneeEntityList.size());

        TaskInstanceQueryByAssigneeParam taskInstanceQueryByAssigneeParam = new TaskInstanceQueryByAssigneeParam();
        taskInstanceQueryByAssigneeParam.setTenantId(tenantId);
        taskInstanceQueryByAssigneeParam.setAssigneeUserId("123");

        List<TaskInstanceEntity> taskInstances = taskDao.findTaskByAssignee(taskInstanceQueryByAssigneeParam);
        Integer count = taskDao.countTaskByAssignee(taskInstanceQueryByAssigneeParam);

        Assert.assertEquals(1, taskInstances.size());
        Assert.assertEquals(1, count.intValue());
    }
}
