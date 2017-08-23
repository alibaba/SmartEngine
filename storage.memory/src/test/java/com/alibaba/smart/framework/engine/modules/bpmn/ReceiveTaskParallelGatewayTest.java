package com.alibaba.smart.framework.engine.modules.bpmn;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ActivityInstance;
import com.alibaba.smart.framework.engine.model.instance.ExecutionInstance;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ActivityInstanceQueryService;
import com.alibaba.smart.framework.engine.service.query.ExecutionInstanceQueryService;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReceiveTaskParallelGatewayTest {

    @Test
    public void testParallelGateway() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        //2.获得常用服务
        ProcessCommandService processCommandService = smartEngine.getProcessCommandService();
        ExecutionCommandService executionCommandService = smartEngine.getExecutionCommandService();

        ActivityInstanceQueryService activityQueryService = smartEngine.getActivityQueryService();
        ExecutionInstanceQueryService executionQueryService = smartEngine.getExecutionQueryService();


        RepositoryCommandService repositoryCommandService = smartEngine
                .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryCommandService
                .deploy("test-receivetask-parallel-gateway.bpmn20.xml");
        assertEquals(14, processDefinition.getProcess().getElements().size());



        Map<String, Object> request = new HashMap<String, Object>();
        request.put("input", 7);
        ProcessInstance processInstance = processCommandService.start(
                processDefinition.getId(), processDefinition.getVersion(),
                request);



        // 流程启动后,正确状态断言
        Assert.assertNotNull(processInstance);
        List<ActivityInstance>  activityInstances =  activityQueryService.findAll(processInstance.getInstanceId());
        Assert.assertNotNull(activityInstances);
        int size = activityInstances.size();
        assertEquals(4, size);

        List<ExecutionInstance> executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        assertEquals(2, executionInstanceList.size());

        ExecutionInstance firstExecutionInstance = executionInstanceList.get(0);
        String firstActivityId=firstExecutionInstance.getActivityId();

        ExecutionInstance secondExecutionInstance = executionInstanceList.get(1);
        String secondActivityId=secondExecutionInstance.getActivityId();

        assertTrue(("theTask1".equals(firstActivityId) && "theTask2".equals(secondActivityId)) || ("theTask1".equals(secondActivityId) && "theTask2".equals(firstActivityId)));

        //完成两个任务节点创建,已经进入fork节点后面的数据。完成其中一个节点的驱动。
        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), null);

        String runningActivityId=secondActivityId;

        executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        assertEquals(2, executionInstanceList.size());

        firstExecutionInstance = executionInstanceList.get(0);
        firstActivityId=firstExecutionInstance.getActivityId();

        secondExecutionInstance = executionInstanceList.get(1);
        secondActivityId=secondExecutionInstance.getActivityId();

        ExecutionInstance runningExecutionInstance;
        if(firstActivityId.equals("join")){
            assertEquals(runningActivityId,secondActivityId);
            runningExecutionInstance=secondExecutionInstance;
        }else{
            assertTrue(runningActivityId.equals(firstActivityId) ||"join".equals(secondActivityId));
            runningExecutionInstance=firstExecutionInstance;
        }

        activityInstances =  activityQueryService.findAll(processInstance.getInstanceId());
        Assert.assertNotNull(activityInstances);
        size = activityInstances.size();
        assertEquals(5, size);



        //完成后面一个节点的驱动。
        request.put("input", 11);
        processInstance = executionCommandService.signal(runningExecutionInstance.getInstanceId(), request);

        executionInstanceList =executionQueryService.findActiveExecution(processInstance.getInstanceId());
        firstExecutionInstance = executionInstanceList.get(0);
        assertEquals(1, executionInstanceList.size());
        assertTrue("theTask3".equals(firstExecutionInstance.getActivityId()));

        processInstance = executionCommandService.signal(firstExecutionInstance.getInstanceId(), request);
        Assert.assertNotNull(processInstance.getCompleteDate());
        assertEquals(InstanceStatus.completed, processInstance.getStatus());


    }


}