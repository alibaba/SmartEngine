package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TaskAssigneeInstanceDAOTest extends BaseElementTest {

    @Resource
    TaskInstanceDAO taskInstanceDAO;

    @Resource
    TaskAssigneeDAO taskAssigneeDAO;

    TaskAssigneeEntity entityOne = null;
    TaskAssigneeEntity entityTwo = null;
    TaskAssigneeEntity entityThree = null;

    @Before
    public void before() {
        entityOne = new TaskAssigneeEntity();
        entityOne.setAssigneeId("123");
        entityOne.setAssigneeType("user");
        entityOne.setId(1001L);
        entityOne.setProcessInstanceId(1000001L);
        entityOne.setTaskInstanceId(2000001L);

        entityTwo = new TaskAssigneeEntity();
        entityTwo.setAssigneeId("abc");
        entityTwo.setAssigneeType("group");
        entityTwo.setId(1002L);
        entityTwo.setProcessInstanceId(1000001L);
        entityTwo.setTaskInstanceId(2000001L);

        entityThree = new TaskAssigneeEntity();
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
