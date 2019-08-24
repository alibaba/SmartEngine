package com.alibaba.smart.framework.engine.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.Activity;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.simulation.ProcessSimulation;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleProcessTest {


    @After
    public void clear(){
        PersisterSession.destroySession();
    }


    @Test
    public void test() throws Exception {

        PersisterSession.create();

        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();


        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        ProcessSimulation  processSimulation = new ProcessSimulation(smartEngine);

        //3. 部署流程定义
        RepositoryCommandService repositoryCommandService = smartEngine
            .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("simple-process-simulation.bpmn.xml");
        assertEquals(14, processDefinition.getProcess().getElements().size());


    }



}