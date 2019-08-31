package com.alibaba.smart.framework.engine.test.cases;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.test.AliPayIdGenerator;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  09:52.
 */
public class BPMNDiagramParserTest extends BaseTestCase {


    @Test
    public void test() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("BPMNDiagramParserTest.bpmn.xml");
        assertEquals(5, processDefinition.getProcess().getElements().size());

        //        PersisterSession.destroySession();

    }
}
