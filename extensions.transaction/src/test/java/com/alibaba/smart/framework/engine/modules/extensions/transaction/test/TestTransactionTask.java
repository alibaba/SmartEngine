package com.alibaba.smart.framework.engine.modules.extensions.transaction.test;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Leo.yy   Created on 2017/12/6.
 * @description
 * @see
 */
public class TestTransactionTask {

    @Before
    public void init() {
        new ClassPathXmlApplicationContext("application-context-test.xml");
        System.setProperty("smart.engine.extensions.transaction.max.retry", "0");
    }


    /**
     * 正常的订单创建流程
     */
    @Test
    public void testNormalWorkFlow() {
        Map<String, Object> request = new HashMap<String, Object>();
        executeSmartEngineFlow("test-transaction-normal.xml", request);
    }


    /**
     * 调用CP创建订单失败后会自动回滚
     * @throws Exception
     */
    @Test
    public void testCreateFailRollback() throws Exception{
        Map<String, Object> request = new HashMap<String, Object>();
        executeSmartEngineFlow("test-transaction-create-fail-rollback.xml", request);
        waitForMsg();
    }

    /**
     * 调用CP创建订单失败后会不断重试，直到成功或者超过重试次数(默认20次）
     * @throws Exception
     */
    @Test
    public void testCreateFailRedo() throws Exception {
        Map<String, Object> request = new HashMap<String, Object>();
        executeSmartEngineFlow("test-transaction-create-fail-redo.xml", request);
        waitForMsg();
    }

    private void executeSmartEngineFlow(String flowDefinitionFile, Map<String, Object> request) {

        PersisterSession.create();

        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        // id doesn't make any sense here
        processEngineConfiguration.setIdGenerator(new com.alibaba.smart.framework.engine.configuration.impl.DefaultIdGenerator());


        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();


        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy(flowDefinitionFile);

        //4.启动流程实例


        processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(), request
        );
    }


    /**
     * 等待Metaq消息回传回来
     */
    private void waitForMsg() {
        try {
                Thread.sleep(1000l);
        } catch (Exception e) {
        }

    }
}
