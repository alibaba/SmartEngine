package com.alibaba.smart.framework.engine.persister.database.dao;

import java.util.Arrays;
import java.util.List;

import com.alibaba.smart.framework.engine.common.util.DateUtil;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TaskAssigneeInstanceDAOTest extends BaseElementTest {

    @Setter(onMethod=@__({@Autowired}))
    TaskInstanceDAO taskInstanceDAO;

    @Setter(onMethod=@__({@Autowired}))
    TaskAssigneeDAO taskAssigneeDAO;

    TaskAssigneeEntity entityOne = null;
    TaskAssigneeEntity entityTwo = null;
    TaskAssigneeEntity entityThree = null;

    @Before
    public void before() {
        entityOne = new TaskAssigneeEntity();

        entityOne.setGmtCreate(DateUtil.getCurrentDate());
        entityOne.setGmtModified(DateUtil.getCurrentDate());

        entityOne.setAssigneeId("123");
        entityOne.setAssigneeType("user");
        entityOne.setId(1001L);
        entityOne.setProcessInstanceId(1000001L);
        entityOne.setTaskInstanceId(2000001L);

        entityTwo = new TaskAssigneeEntity();

        entityTwo.setGmtCreate(DateUtil.getCurrentDate());
        entityTwo.setGmtModified(DateUtil.getCurrentDate());
        entityTwo.setAssigneeId("abc");
        entityTwo.setAssigneeType("group");
        entityTwo.setId(1002L);
        entityTwo.setProcessInstanceId(1000001L);
        entityTwo.setTaskInstanceId(2000001L);

        entityThree = new TaskAssigneeEntity();
        entityThree.setGmtCreate(DateUtil.getCurrentDate());
        entityThree.setGmtModified(DateUtil.getCurrentDate());
        entityThree.setAssigneeId("abc");
        entityThree.setAssigneeType("group");
        entityThree.setId(1003L);
        entityThree.setProcessInstanceId(99991L);
        entityThree.setTaskInstanceId(2000002L);
    }

    @Test
    public void testInsert() {
        taskAssigneeDAO.insert(entityOne);
        taskAssigneeDAO.insert(entityTwo);

        Assert.assertNotNull(entityOne);
    }

    @Test
    public void testFindOne() {
        taskAssigneeDAO.insert(entityOne);

        TaskAssigneeEntity result = taskAssigneeDAO.findOne(entityOne.getId());
        Assert.assertNotNull(result);
    }

    @Test
    public void testDelete() {
        taskAssigneeDAO.insert(entityOne);

        TaskAssigneeEntity result = taskAssigneeDAO.findOne(entityOne.getId());
        Assert.assertNotNull(result);

        taskAssigneeDAO.delete(entityOne.getId());

        result = taskAssigneeDAO.findOne(entityOne.getId());
        Assert.assertNull(result);
    }

/*    @Test
    public void testUpdate() {
        taskAssigneeDAO.insert(entityOne);

        taskAssigneeDAO.update(entityOne.getId(), "456");

        TaskAssigneeEntity result = taskAssigneeDAO.findOne(entityOne.getId());
        Assert.assertEquals("456", result.getAssigneeId());
    }*/

    @Test
    public void testFindList(){
        taskAssigneeDAO.insert(entityOne);
        taskAssigneeDAO.insert(entityTwo);
        taskAssigneeDAO.insert(entityThree);

        Assert.assertEquals(2,taskAssigneeDAO.findList(2000001L).size());
        Assert.assertEquals(1,taskAssigneeDAO.findList(2000002L).size());
    }

    @Test
    public void testFindListForInstanceList(){
        taskAssigneeDAO.insert(entityOne);
        taskAssigneeDAO.insert(entityTwo);
        taskAssigneeDAO.insert(entityThree);

        Long[] array = {2000001L,2000002L};
        List<TaskAssigneeEntity> entityList = taskAssigneeDAO.findListForInstanceList(Arrays.asList(array));

        Assert.assertEquals(3,entityList.size());
    }
}
