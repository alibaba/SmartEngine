package com.alibaba.smart.framework.engine.test.cases;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.test.AliPayIdGenerator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SmartPropertiesTest extends BaseTestCase {


    @Test
    public void test() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("smartPropertiesTest.bpmn.xml");
        assertEquals(10, processDefinition.getProcess().getElements().size());

        //4.启动流程实例
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("input", "right");

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), request
        );
        Assert.assertNotNull(processInstance);
        Assert.assertTrue(processInstance.getStatus().equals(InstanceStatus.completed));

    }

}