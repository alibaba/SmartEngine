package com.alibaba.smart.framework.engine.test;

import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.alibaba.smart.framework.engine.service.RepositoryService;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * 测试 process Service
 * @author dongdong.zdd
 * @since 2016-12-13
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class ProcessTest {



    @Test
    public void testAbortSignalEvent() {
        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();

        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);
        RepositoryService repositoryService = smartEngine
                .getRepositoryService();
        ProcessDefinition processDefinition = repositoryService
                .deploy("test-demo.bpmn20.xml");


        ProcessService processService = smartEngine.getProcessService();

        Map<String,Object> request = Maps.newHashMap();
        request.put("1","1");
        ProcessInstance start = processService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                request);





        request.put("2","2");
        request.put("event","testAbort");
        ProcessInstance select = processService.run(processDefinition,
                start.getInstanceId(),"createOrder",false,request);


        System.out.printf(select.toString());

        Assert.assertNotNull(start);
        Assert.assertNotNull(select);


    }

}
