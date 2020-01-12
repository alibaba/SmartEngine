package com.alibaba.smart.framework.engine.test.process;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.model.instance.TaskInstance;
import com.alibaba.smart.framework.engine.test.DatabaseBaseTestCase;
import com.alibaba.smart.framework.engine.test.process.helper.TransactionHelper;
import com.alibaba.smart.framework.engine.test.process.task.dispatcher.DefaultTaskAssigneeDispatcher;

import org.junit.Assert;
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
    protected void initProcessConfiguration() {
        super.initProcessConfiguration();
        //processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new DefaultTaskAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setVariablePersister(new CustomVariablePersister());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
    }


    @Autowired
    private TransactionHelper transactionHelper;

    @Test(expected = IllegalArgumentException.class)
   //@Test
    public void exception() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("usertask-servicetask-rollback.bpmn20.xml").getFirstProcessDefinition();

        ProcessInstance processInstance = transactionHelper.start(processCommandService, processDefinition);

        TaskInstance submitTaskInstance = transactionHelper.signal(taskCommandService, taskQueryService, processInstance);

       //10.由于流程测试已经关闭,需要断言没有需要处理的人,状态关闭.
       ProcessInstance finalProcessInstance = processQueryService.findById(submitTaskInstance.getProcessInstanceId());
       Assert.assertEquals(InstanceStatus.completed,finalProcessInstance.getStatus());
    }





}