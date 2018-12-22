package com.alibaba.smart.framework.engine.test.process;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.constant.TaskInstanceConstant;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.persister.database.dao.TaskInstanceDAO;
import com.alibaba.smart.framework.engine.persister.database.entity.TaskInstanceEntity;
import com.alibaba.smart.framework.engine.persister.database.util.SpringContextUtil;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.test.process.helper.TransactionHelper;
import com.alibaba.smart.framework.engine.test.process.task.dispatcher.DefaultTaskAssigneeDispatcher;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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