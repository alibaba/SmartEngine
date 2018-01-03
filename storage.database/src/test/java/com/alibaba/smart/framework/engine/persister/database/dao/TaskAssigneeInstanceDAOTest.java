package com.alibaba.smart.framework.engine.persister.database.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskAssigneeEntity;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;

import com.google.common.collect.Lists;
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
    TaskInstanceEntity taskInstanceEntityThree = null;

    TaskAssigneeEntity entityOne = null;
    TaskAssigneeEntity entityTwo = null;
    TaskAssigneeEntity entityThree = null;

    private String TEST_PROCESS_DEFINITION_TYPE="testProcessDefinitionType";

    private long testId = 2000342L;
    @Before
    public void before() {

        entityOne = new TaskAssigneeEntity();
        entityOne.setAssigneeId("123");
        entityOne.setAssigneeType("user");
        //entityOne.setId(1001L);
        entityOne.setProcessInstanceId(1000001L);
        entityOne.setTaskInstanceId(2000001L);

        entityTwo = new TaskAssigneeEntity();
        entityTwo.setAssigneeId("abc");
        entityTwo.setAssigneeType("group");
        //entityTwo.setId(1002L);
        entityTwo.setProcessInstanceId(1000001L);
        entityTwo.setTaskInstanceId(2000001L);

        entityThree = new TaskAssigneeEntity();
        entityThree.setAssigneeId("abc");
        entityThree.setAssigneeType("group");
        //entityThree.setId(1003L);
        entityThree.setProcessInstanceId(99991L);
        entityThree.setTaskInstanceId(testId);


        taskInstanceEntityThree = new TaskInstanceEntity();
        taskInstanceEntityThree.setProcessInstanceId(0L);
        taskInstanceEntityThree.setProcessDefinitionIdAndVersion("");
        taskInstanceEntityThree.setExecutionInstanceId(0L);
        taskInstanceEntityThree.setActivityInstanceId(0L);
        taskInstanceEntityThree.setProcessDefinitionType(TEST_PROCESS_DEFINITION_TYPE);
        taskInstanceEntityThree.setProcessDefinitionActivityId("");
        taskInstanceEntityThree.setClaimUserId("");
        taskInstanceEntityThree.setPriority(0);
        taskInstanceEntityThree.setStatus("");
        taskInstanceEntityThree.setTag("");
        taskInstanceEntityThree.setClaimTime(new Date());
        taskInstanceEntityThree.setCompleteTime(new Date());
        taskInstanceEntityThree.setComment("");
        taskInstanceEntityThree.setExtension("");
        taskInstanceEntityThree.setTitle("");
        taskInstanceEntityThree.setId(testId);
        taskInstanceEntityThree.setGmtCreate(new Date());
        taskInstanceEntityThree.setGmtModified(new Date());


    }

    @Test
    public void testInsert() {
        taskAssigneeDAO.insert(entityOne);
        taskAssigneeDAO.insert(entityTwo);

        Assert.assertNotNull(entityOne);
    }

    @Test
    public void testBatchInsert1() {
        List<TaskAssigneeEntity> taskAssigneeEntityList = Lists.newArrayList(entityOne,entityTwo);
        taskAssigneeDAO.batchInsert(taskAssigneeEntityList);
        for(TaskAssigneeEntity taskAssigneeEntity : taskAssigneeEntityList){
            Assert.assertNotNull(taskAssigneeEntity.getId());
            TaskAssigneeEntity taskAssigneeEntityInDb = taskAssigneeDAO.findOne(taskAssigneeEntity.getId());
            Assert.assertNotNull(taskAssigneeEntity.getAssigneeId(),taskAssigneeEntityInDb.getAssigneeId());
            taskAssigneeDAO.delete(taskAssigneeEntity.getId());
        }
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
        Assert.assertEquals(1,taskAssigneeDAO.findList(testId).size());
    }

    @Test
    public void testFindListForInstanceList(){
        taskAssigneeDAO.insert(entityOne);
        taskAssigneeDAO.insert(entityTwo);
        taskAssigneeDAO.insert(entityThree);

        Long[] array = {2000001L,testId};
        List<TaskAssigneeEntity> entityList = taskAssigneeDAO.findListForInstanceList(Arrays.asList(array));

        Assert.assertEquals(3,entityList.size());
    }

    @Test
    public void testInsertAndFindByDefinitionTypeList(){
        taskAssigneeDAO.insert(entityThree);
        taskInstanceDAO.insert(taskInstanceEntityThree);

        TaskInstanceQueryByAssigneeParam taskInstanceQueryByAssigneeParam = new TaskInstanceQueryByAssigneeParam();
        taskInstanceQueryByAssigneeParam.setProcessDefinitionTypeList(Lists.<String>newArrayList(TEST_PROCESS_DEFINITION_TYPE));
        taskInstanceQueryByAssigneeParam.setAssigneeGroupIdList(Lists.<String>newArrayList("abc"));
        List<TaskInstanceEntity> entityList = taskInstanceDAO.findTaskByAssignee(taskInstanceQueryByAssigneeParam);
        Assert.assertTrue(entityList.size()==1);
        Integer count= taskInstanceDAO.countTaskByAssignee(taskInstanceQueryByAssigneeParam);
        Assert.assertTrue(count==1);

        taskInstanceQueryByAssigneeParam.setProcessDefinitionTypeList(Lists.<String>newArrayList("what?"));
        taskInstanceQueryByAssigneeParam.setAssigneeGroupIdList(Lists.<String>newArrayList("abc"));
        entityList = taskInstanceDAO.findTaskByAssignee(taskInstanceQueryByAssigneeParam);
        Assert.assertTrue(entityList.size()==0);
        count= taskInstanceDAO.countTaskByAssignee(taskInstanceQueryByAssigneeParam);
        Assert.assertTrue(count==0);

    }
}
