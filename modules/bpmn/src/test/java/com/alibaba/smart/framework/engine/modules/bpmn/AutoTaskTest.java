package com.alibaba.smart.framework.engine.modules.bpmn;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.param.EngineParam;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.alibaba.smart.framework.engine.service.RepositoryService;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author dongdong.zdd
 * @since 2017-04-18
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class AutoTaskTest {


    @Getter
    public static ProcessTest processTest = new ProcessTest();


    @Resource
    private Workflow workflow;


    @Test
    public void testRunMideng() throws Exception {

        ProcessInstance processInstance = null;
        try {
            ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
            DefaultSmartEngine smartEngine = new DefaultSmartEngine();
            smartEngine.init(processEngineConfiguration);

            RepositoryService repositoryService = smartEngine.getRepositoryService();
            ProcessDefinition processDefinition = repositoryService.deploy("test-auto.bpmn20.xml");

            ProcessService processService = smartEngine.getProcessService();
            Map<String,Object> context = Maps.newHashMap();
            context.put("1","1");
            processInstance = processService.start(processDefinition.getId(), processDefinition.getVersion(), context);
        }finally {
            System.out.println(processInstance.toString());
        }

    }


    /**
     *
     * 1. 流程引擎开始
     * 2. 自动节点第一个节点执行成功
     * 3. 自动节点第二个节点执行失败,抛异常.
     * 4. 判断当前引擎推进位置点
     * @throws Exception
     */
    @Test
    public void test_run_abort_exception() throws Exception {

        ProcessInstance processInstance = null;
        try {
            ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
            DefaultSmartEngine smartEngine = new DefaultSmartEngine();
            smartEngine.init(processEngineConfiguration);

            RepositoryService repositoryService = smartEngine.getRepositoryService();
            ProcessDefinition processDefinition = repositoryService.deploy("test-auto-abort.bpmn20.xml");

            ProcessService processService = smartEngine.getProcessService();
            Map<String,Object> context = Maps.newHashMap();
            context.put("1","1");
            processInstance = processService.start(processDefinition.getId(), processDefinition.getVersion(), context);

            processInstance  = processService.run(processDefinition,processInstance,"createOrder",context);

        }finally {
            System.out.println(processInstance.toString());
        }

    }


    /**
     * 1. 流程引擎中断
     * 2. 存储当前位置状态
     * 3. 从当前位置开始运行
     * @throws Exception
     */
    @Test
    public void testAbortAutoProcess() throws Exception {


        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();
        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        RepositoryService repositoryService = smartEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.deploy("test-auto-abort.bpmn20.xml");

        ProcessService processService = smartEngine.getProcessService();
        Map<String,Object> context = Maps.newHashMap();
        context.put("1","1");
        ProcessInstance processInstance  = null;
        try {
            processInstance = processService.start(processDefinition.getId(), processDefinition.getVersion(), context);
            processInstance  = processService.run(processDefinition,processInstance,"createOrder",context);

        }catch (Exception e) {
            System.out.println(processInstance.toString());
        }


        processTest.setProcessStore(processInstance.toString());

        EngineParam engineParam  = EngineParam.of("1","test-parallel","1.0.0",processTest.getProcessStore());
        ProcessInstance re = processService.recovery(engineParam);
        processInstance  = processService.run(processDefinition,re,"theTask2",context);

        System.out.println(processInstance.toString());

    }



    /**
     * 1. 流程引擎开始
     * 2. 自动节点第一个节点执行成功
     * 3. push当前的流程实例
     * 3. 自动节点第二个节点
     * 4. 判断当前引擎推进位置点
     * @throws Exception
     */
    @Test
    public void test_process_push() throws Exception {


        SmartEngine smartEngine = workflow.getEngine();
        RepositoryService repositoryService = smartEngine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.deploy("test-auto.bpmn20.xml");

        ProcessService processService = smartEngine.getProcessService();
        Map<String,Object> context = Maps.newHashMap();
        context.put("1","1");
        context.put("processDefinition",processDefinition);
        ProcessInstance processInstance  = null;


        try {
            processInstance = processService.start(processDefinition.getId(), processDefinition.getVersion(), context);
            context.put("id",processInstance.getInstanceId());
            processInstance  = processService.run(processDefinition,processInstance,"createOrder",context);



        }catch (Exception e) {
            System.out.println(processInstance.toString());
        }




        processTest.setProcessStore(processInstance.toString());

        EngineParam engineParam  = EngineParam.of("1","test-parallel","1.0.0",processTest.getProcessStore());
        ProcessInstance re = processService.recovery(engineParam);
        processInstance  = processService.run(processDefinition,re,"theTask2",context);

        System.out.println(processInstance.toString());

    }

}
