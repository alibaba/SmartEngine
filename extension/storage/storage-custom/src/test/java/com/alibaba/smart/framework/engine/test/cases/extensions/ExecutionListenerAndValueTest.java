package com.alibaba.smart.framework.engine.test.cases.extensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class ExecutionListenerAndValueTest extends CustomBaseTestCase {

    public static List<String> trace = new ArrayList<String>();

    @Test
    public void testDemo() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("execution_listener_and_value_test.bpmn20.xml").getFirstProcessDefinition();
        Assert.assertEquals(7, processDefinition.getBaseElementList().size());

        ProcessCommandService processService = smartEngine.getProcessCommandService();

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("hello", "world1");

        Map<String, Object> response = new HashMap<String, Object>();


        ProcessInstance processInstance = processService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request,response);

        Assert.assertNotNull(processInstance);
        Assert.assertEquals(processInstance.getStatus(), InstanceStatus.completed);


        //Assert.assertEquals(6,response.size());
        Assert.assertEquals("start_listener",response.get("start"));
        Assert.assertEquals("end_listener",response.get("end"));
        Assert.assertEquals("koubei",response.get("from"));
        Assert.assertEquals("Create task",response.get("task1"));
        Assert.assertEquals("Pay task",response.get("task2"));

        //验证几个事件的的执行顺序
        Long processStartTime = (Long)response.get("processStartTime");
        Long startTime = (Long)response.get("startTime");
        Long endTime = (Long)response.get("endTime");
        Long processEndTime = (Long)response.get("processEndTime");
        Assert.assertTrue((processStartTime - startTime) < 0);
        Assert.assertTrue((startTime - endTime) < 0);
        Assert.assertTrue((endTime - processEndTime) < 0);

        //验证 process_start 和 process_end 仅在 startEvent 和 endEvent中有效
        AtomicInteger processStartCount = (AtomicInteger)response.get("processStartCount");
        AtomicInteger processEndCount = (AtomicInteger)response.get("processEndCount");

        Assert.assertTrue(processStartCount.get() == 1);
        Assert.assertTrue(processEndCount.get() == 1);





    }


}
