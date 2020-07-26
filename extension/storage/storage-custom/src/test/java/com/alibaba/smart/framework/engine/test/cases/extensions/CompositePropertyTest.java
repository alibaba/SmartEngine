package com.alibaba.smart.framework.engine.test.cases.extensions;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.InstanceStatus;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.smart.PropertyCompositeKey;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CompositePropertyTest extends CustomBaseTestCase {


    @Test
    public void test() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("CompositePropertyTest.bpmn.xml").getFirstProcessDefinition();
        assertEquals(8, processDefinition.getBaseElementList().size());

        ExtensionElements extensionElements = processDefinition.getExtensionElements();
        Map map = (Map)extensionElements.getDecorationMap().get(ExtensionElementsConstant.PROPERTIES);

        PropertyCompositeKey key = new PropertyCompositeKey("json", "key");

        boolean flag = map.containsKey(key);
        Assert.assertTrue(flag);

        Object value = map.get(key);
        Assert.assertEquals("{}",value);

    }

}