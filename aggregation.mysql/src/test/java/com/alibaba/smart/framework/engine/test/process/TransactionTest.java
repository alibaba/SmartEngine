package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.persister.common.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.test.process.helper.TransactionHelper;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TransactionTest {


    //@Transactional
    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("/spring/application-test.xml");
        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) SpringContextUtil.getBean("taskInstanceDAO");
        //ya(taskInstanceDAO);

        TransactionHelper transactionHelper= (TransactionHelper) SpringContextUtil.getBean("transactionHelper");


        transactionHelper.ya(taskInstanceDAO);

        return;
    }

    //@Transactional
    //public static void ya(TaskInstanceDAO taskInstanceDAO) {
    //    TaskInstanceEntity entity = new TaskInstanceEntity();
    //    entity.setId(System.currentTimeMillis());
    //
    //    entity.setProcessDefinitionIdAndVersion("processDefinitionId");
    //    entity.setActivityInstanceId(11L);
    //    entity.setClaimUserId("assign_id");
    //    Date claimTime = new Date();
    //    entity.setClaimTime(new Date(claimTime.getTime()));
    //    entity.setCompleteTime(new Date(claimTime.getTime() + 1000000));
    //    entity.setExecutionInstanceId(22L);
    //    entity.setPriority(333);
    //    entity.setStatus(TaskInstanceConstant.PENDING);
    //    entity.setProcessDefinitionActivityId("userTask");
    //    entity.setProcessInstanceId(444L);
    //    entity.setComment("comment");
    //    entity.setExtension("extension");
    //    entity.setTitle("title");
    //
    //    taskInstanceDAO.insert(entity);
    //
    //    throw new RuntimeException("ya");
    //}

}