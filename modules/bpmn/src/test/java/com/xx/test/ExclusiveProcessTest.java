package com.xx.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.process.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.process.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.process.engine.ProcessEngine;
import com.alibaba.smart.framework.process.engine.impl.DefaultProcessEngine;
import com.alibaba.smart.framework.process.model.runtime.command.impl.ProcessInstanceStartCommand;
import com.alibaba.smart.framework.process.service.RuntimeService;

/**
 * Base Smart Engine Test Created by ettear on 16-4-14.
 */
public class ExclusiveProcessTest {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void test() throws Exception {
        
        ProcessEngine processEngine = new DefaultProcessEngine();
        List<String> processDefinitions= new ArrayList<>(1);
        processDefinitions.add("test-exclusive.bpmn20.xml");
        ProcessEngineConfiguration processEngineConfiguration= new DefaultProcessEngineConfiguration(processDefinitions);;
        processEngine.init(processEngineConfiguration);

        RuntimeService runtimeService =    processEngine.getRuntimeService();
        
        ProcessInstanceStartCommand command = new ProcessInstanceStartCommand();
        command.setProcessDefinitionId("test-exclusive-my");
        command.setVersion("1.0.0");
        
        ProcessInstance processInstance =   runtimeService.start(command );
        
        Assert.assertNotNull(processInstance);
       
        // TODO 校验每个元素都是ok

    }
}
