package com.alibaba.smart.framework.engine.test.cases.extensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.smart.PropertyCompositeKey;
import com.alibaba.smart.framework.engine.smart.PropertyCompositeValue;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class ActivitiDelegationAndExecutionListenerAndValueTest extends CustomBaseTestCase {

    public static List<String> trace = new ArrayList<String>();

    @Test
    public void testDemo() throws Exception {

        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("activiti.bpmn.xml").getFirstProcessDefinition();
        Assert.assertEquals(10, processDefinition.getBaseElementList().size());

        ProcessCommandService processService = smartEngine.getProcessCommandService();

        Map<String, Object> request = new HashMap<String, Object>();
        request.put("flag",0);

        Map<String, Object> response = new HashMap<String, Object>();


        ProcessInstance processInstance = processService.start(
            processDefinition.getId(), processDefinition.getVersion(),
            request,response);

        Assert.assertNotNull(processInstance);
        Assert.assertEquals(processInstance.getStatus(), InstanceStatus.completed);


        //Assert.assertEquals(6,response.size());
        Assert.assertEquals("start_listener",response.get("start"));
        Assert.assertEquals("end_listener",response.get("end"));

        PropertyCompositeKey from = new PropertyCompositeKey("from");

        PropertyCompositeValue actual = (PropertyCompositeValue)response.get(from);
        Assert.assertEquals("koubei", actual.getAttrMap().get("value"));

    }


}
