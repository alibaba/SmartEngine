package com.alibaba.smart.framework.engine.test.cases.extensions;

import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.smart.Properties;
import com.alibaba.smart.framework.engine.smart.PropertyCompositeKey;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CompositePropertiesTest extends CustomBaseTestCase {


    @Test
    public void test() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("CompositePropertyTest.bpmn.xml").getFirstProcessDefinition();
        assertEquals(8, processDefinition.getBaseElementList().size());

        ExtensionElements extensionElements = processDefinition.getExtensionElements();
        Map<String, Object> decorationMap = extensionElements.getDecorationMap();

        Map<PropertyCompositeKey,Map> propertyCompositeKeyMap = (Map) decorationMap.get(ExtensionElementsConstant.PROPERTIES);

        List<ExtensionDecorator> extensionList = extensionElements.getExtensionList();
        Assert.assertEquals(1,extensionList.size());
        Properties properties = (Properties) extensionList.get(0);
        Assert.assertEquals(5,properties.getExtensionList().size());



        PropertyCompositeKey key = new PropertyCompositeKey("json", "key");

        boolean flag = propertyCompositeKeyMap.containsKey(key);
        Assert.assertTrue(flag);

        Map value = propertyCompositeKeyMap.get(key);
        Assert.assertEquals("{}",value.get("value"));

        PropertyCompositeKey key2 = new PropertyCompositeKey("action", "key");
        Map value2 = propertyCompositeKeyMap.get(key2);
        Assert.assertEquals("blabla1",value2.get("attr1"));
        Assert.assertEquals("blabla2",value2.get("attr2"));


    }

}