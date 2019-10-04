package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.command.TaskCommandService;
import com.alibaba.smart.framework.engine.service.query.ProcessQueryService;
import com.alibaba.smart.framework.engine.service.query.TaskQueryService;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.TransactionHelper;
import com.alibaba.smart.framework.engine.test.process.task.dispatcher.DefaultTaskAssigneeDispatcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MysqlTransactionTest extends DatabaseBaseTestCase {

    @Override
    protected void initProcessConfiguation() {
        super.initProcessConfiguation();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setLockStrategy(new DefaultLockStrategy());
    }


    @Autowired
    private TransactionHelper transactionHelper;

    @Test(expected = IllegalArgumentException.class)

    public void passed() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("usertask-servicetask-rollback.bpmn20.xml");

        ProcessInstance processInstance = transactionHelper.start(processCommandService, processDefinition);

        //TUNE 增加对事务的查询判断。
        TaskInstance submitTaskInstance = transactionHelper.signal(taskCommandService, taskQueryService, processInstance);

        transactionHelper.a(processQueryService, submitTaskInstance);

    }





}