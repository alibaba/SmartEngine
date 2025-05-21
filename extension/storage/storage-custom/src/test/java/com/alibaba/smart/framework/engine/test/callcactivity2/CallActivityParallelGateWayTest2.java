package com.alibaba.smart.framework.engine.test.callcactivity2;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.common.util.StringUtil;
import com.alibaba.smart.framework.engine.configuration.LockStrategy;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
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
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class CallActivityParallelGateWayTest2 {
    private SmartEngine smartEngine;
    ExecutionQueryService executionQueryService;
    ExecutionCommandService executionCommandService;

    String tenantId = "-1";

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
            .deploy("call-activity2/call_act_parent1.bpmn",tenantId);
        repositoryCommandService
            .deploy("call-activity2/call_act_sub_1.bpmn",tenantId);
        repositoryCommandService
            .deploy("call-activity2/call_act_sub_2.bpmn",tenantId);
    }

    @Test
    public void test() {
        PersisterSession.create();
        PersisterSession session = PersisterSession.currentSession();
        Assert.assertNotNull(session);

        ProcessInstance processInstance = smartEngine.getProcessCommandService().start(
            "yanricheng_parent_1", "1.0.0", tenantId
        );

        Assert.assertNotNull(processInstance);

        String parentProcessInstanceId = processInstance.getInstanceId();

        // ==== Assert Task 1 ====
        Assert.assertEquals(1, session.getProcessInstances().size());

        ProcessInstanceQueryParam procInstQuery = new ProcessInstanceQueryParam();
        procInstQuery.setTenantId(tenantId);
        List<ProcessInstance> procInstList = this.smartEngine.getProcessQueryService().findList(procInstQuery);
        Assert.assertEquals(1, procInstList.size());
        Assert.assertEquals(processInstance.getInstanceId(), procInstList.get(0).getInstanceId());
        Assert.assertNull(processInstance.getParentInstanceId());
        Assert.assertNull(processInstance.getParentExecutionInstanceId());

        //执行到：receive_task_1
        List<ExecutionInstance>  parentExecutionInstances = this.executionQueryService.findActiveExecutionList(parentProcessInstanceId);
        Assert.assertEquals(1, parentExecutionInstances.size());

        // ==== Signal receive_task_1 ====
        Map<String,Object> request = new HashMap<>();
        request.put(RequestMapSpecialKeyConstant.TENANT_ID,tenantId);
        this.executionCommandService.signal(parentExecutionInstances.get(0).getInstanceId(),request);

        List<ActivityInstance> actList1 =  smartEngine.getActivityQueryService().findAll(parentProcessInstanceId);
        List<ActivityInstance> actList2 = smartEngine.getActivityQueryService().findAll(parentProcessInstanceId,tenantId);
        Assert.assertEquals(actList1.size(), actList2.size());
        Assert.assertEquals(7, actList1.size());

        //此时生成3个流程实例
        Assert.assertEquals(3, session.getProcessInstances().size());
        //
        List<ExecutionInstance> activeExecutionInstances = smartEngine.getExecutionQueryService().findActiveExecutionList(parentProcessInstanceId,tenantId);

        Assert.assertEquals(4, activeExecutionInstances.size());

        //找到所有的子流程实例
        List<ProcessInstance> subProcessInstanceList =  session.getProcessInstances().values().stream()
                .filter(entry -> StringUtil.isNotEmpty(entry.getParentInstanceId())
                        && StringUtil.equals(entry.getParentInstanceId(), parentProcessInstanceId))
                .collect(Collectors.toList());

        Assert.assertEquals(2, subProcessInstanceList.size());

        List<ExecutionInstance>  subExecutionInstances1 = this.executionQueryService.findActiveExecutionList(subProcessInstanceList.get(0).getInstanceId());
        List<ExecutionInstance>  subExecutionInstances2 = this.executionQueryService.findActiveExecutionList(subProcessInstanceList.get(1).getInstanceId());

        //执行完2个子流程
        executionCommandService.signal(subExecutionInstances1.get(0).getInstanceId(),null);
        executionCommandService.signal(subExecutionInstances2.get(0).getInstanceId(),null);

        Assert.assertTrue(InstanceStatus.running == session.getProcessInstances().get(parentProcessInstanceId).getStatus());
        Assert.assertTrue(InstanceStatus.completed == session.getProcessInstances().get(subExecutionInstances2.get(0).getProcessInstanceId()).getStatus());
        Assert.assertTrue(InstanceStatus.completed == session.getProcessInstances().get(subExecutionInstances2.get(0).getProcessInstanceId()).getStatus());


        List<ExecutionInstance> activeExecutionInstances1 = smartEngine.getExecutionQueryService().findActiveExecutionList(parentProcessInstanceId,tenantId);

        Assert.assertEquals(4, activeExecutionInstances1.size());
    }
}


