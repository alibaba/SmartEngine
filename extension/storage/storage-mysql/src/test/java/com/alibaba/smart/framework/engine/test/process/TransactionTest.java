//package com.alibaba.smart.framework.engine.test.process;
//
//
//import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
//import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
//import com.alibaba.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
//import com.alibaba.smart.framework.engine.test.process.helper.CustomVariablePersister;
//import com.alibaba.smart.framework.engine.test.process.helper.DefaultMultiInstanceCounter;
//import com.alibaba.smart.framework.engine.test.process.helper.DoNothingLockStrategy;
//import com.alibaba.smart.framework.engine.test.process.helper.TransactionHelper;
//import com.alibaba.smart.framework.engine.test.process.helper.dispatcher.DefaultTaskAssigneeDispatcher;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.transaction.annotation.Transactional;
//
//@ContextConfiguration("/spring/application-test.xml")
//@RunWith(SpringJUnit4ClassRunner.class)
//@Transactional
//public class TransactionTest extends DatabaseBaseTestCase {
//
//    @Override
//    protected void initProcessConfiguration() {
//        super.initProcessConfiguration();
//        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
//        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
//        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
//        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
//        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
//    }
//
//
//    @Test
//    public void testAuditProcess() throws Exception {
//
//        ApplicationContext context = new ClassPathXmlApplicationContext("/spring/application-test.xml");
//        TaskInstanceDAO taskInstanceDAO= (TaskInstanceDAO) processEngineConfiguration.getInstanceAccessor().access("taskInstanceDAO");
//        //ya(taskInstanceDAO);
//
//        TransactionHelper transactionHelper= (TransactionHelper) processEngineConfiguration.getInstanceAccessor().access("transactionHelper");
//
//
//        transactionHelper.ya(taskInstanceDAO);
//
//        return;
//    }
//
//    //@Transactional
//    //public static void ya(TaskInstanceDAO taskInstanceDAO) {
//    //    TaskInstanceEntity entity = new TaskInstanceEntity();
//    //    entity.setId(System.currentTimeMillis());
//    //
//    //    entity.setProcessDefinitionIdAndVersion("processDefinitionId");
//    //    entity.setActivityInstanceId(11L);
//    //    entity.setClaimUserId("assign_id");
//    //    Date claimTime = new Date();
//    //    entity.setClaimTime(new Date(claimTime.getTime()));
//    //    entity.setCompleteTime(new Date(claimTime.getTime() + 1000000));
//    //    entity.setExecutionInstanceId(22L);
//    //    entity.setPriority(333);
//    //    entity.setStatus(TaskInstanceConstant.PENDING);
//    //    entity.setProcessDefinitionActivityId("userTask");
//    //    entity.setProcessInstanceId(444L);
//    //    entity.setComment("comment");
//    //    entity.setExtension("extension");
//    //    entity.setTitle("title");
//    //
//    //    taskInstanceDAO.insert(entity);
//    //
//    //    throw new RuntimeException("ya");
//    //}
//
//}