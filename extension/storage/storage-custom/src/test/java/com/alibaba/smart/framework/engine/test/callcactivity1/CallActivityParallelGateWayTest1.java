package com.alibaba.smart.framework.engine.test.callcactivity1;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.persister.custom.session.PersisterSession;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.param.query.ProcessInstanceQueryParam;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.test.DoNothingLockStrategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;


public class CallActivityParallelGateWayTest1 {
    ExecutionQueryService executionQueryService;
    ExecutionCommandService executionCommandService;
    private SmartEngine smartEngine;

    @Before
    public void before() {

        this.smartEngine = new DefaultSmartEngine();
        DefaultProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        LockStrategy doNothingLockStrategy = new DoNothingLockStrategy();
        processEngineConfiguration.setLockStrategy(doNothingLockStrategy);
        processEngineConfiguration.setExecutorService(Executors.newFixedThreadPool(10));

        this.smartEngine.init(processEngineConfiguration);
        this.executionQueryService = this.smartEngine.getExecutionQueryService();
        this.executionCommandService = this.smartEngine.getExecutionCommandService();
        this.deploy();
    }


    private void deploy() {
        RepositoryCommandService repositoryCommandService = this.smartEngine
                .getRepositoryCommandService();
        repositoryCommandService
                .deploy("call-activity1/call_act_parent.bpmn");
        repositoryCommandService
                .deploy("call-activity1/call_act_sub.bpmn");
    }

    @Test
    public void test() {
        PersisterSession.create();
        PersisterSession session = PersisterSession.currentSession();
        Assert.assertNotNull(session);

        ProcessInstance processInstance = smartEngine.getProcessCommandService().start(
                "parent", "1.0", new HashMap<String, Object>()
        );

        Assert.assertNotNull(processInstance);

        String parentProcessInstanceId = processInstance.getInstanceId();

        // ==== Assert Task 1 ====
        Assert.assertEquals(2, session.getProcessInstances().size());

        List<ExecutionInstance> parentExecutionInstances = this.executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        Assert.assertEquals(1, parentExecutionInstances.size());

        ProcessInstanceQueryParam processInstanceQueryParam = new ProcessInstanceQueryParam();
        processInstanceQueryParam.setParentInstanceId(parentProcessInstanceId);
        List<ProcessInstance> subProcessInstances = this.smartEngine.getProcessQueryService().findList(processInstanceQueryParam);
        Assert.assertEquals(1, subProcessInstances.size());

        List<ExecutionInstance> subExecutionInstances = this.executionQueryService.findActiveExecutionList(subProcessInstances.get(0).getInstanceId());
        Assert.assertEquals(1, subExecutionInstances.size());

        // ==== Signal Task 1 ====
        ProcessInstance processInstance1 = this.executionCommandService.signal(subExecutionInstances.get(0).getInstanceId(), null);

    }
}


