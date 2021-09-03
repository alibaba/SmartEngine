package com.alibaba.smart.framework.engine.test.cases.extensions;

import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionDecorator;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.smart.Properties;
import com.alibaba.smart.framework.engine.smart.PropertyCompositeKey;
import com.alibaba.smart.framework.engine.smart.PropertyCompositeValue;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CompositePropertiesTest extends CustomBaseTestCase {


    @Test
    public void test() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("CompositePropertyTest.bpmn.xml").getFirstProcessDefinition();
        assertEquals(8, processDefinition.getBaseElementList().size());

        ExtensionElements extensionElements = processDefinition.getExtensionElements();
        Map<String, Object> decorationMap = extensionElements.getDecorationMap();

        Map<PropertyCompositeKey,PropertyCompositeValue> propertyCompositeKeyMap = (Map<PropertyCompositeKey,PropertyCompositeValue>) decorationMap.get(ExtensionElementsConstant.PROPERTIES);

        List<ExtensionDecorator> extensionList = extensionElements.getExtensionList();
        Assert.assertEquals(1,extensionList.size());
        Properties properties = (Properties) extensionList.get(0);
        Assert.assertEquals(5,properties.getExtensionList().size());



        PropertyCompositeKey key = new PropertyCompositeKey("json", "key");

        boolean flag = propertyCompositeKeyMap.containsKey(key);
        Assert.assertTrue(flag);

        PropertyCompositeValue value = propertyCompositeKeyMap.get(key);
        Assert.assertEquals("{}",value.getAttrMap().get("value"));

        PropertyCompositeKey key2 = new PropertyCompositeKey("action", "key");
        PropertyCompositeValue value2 = propertyCompositeKeyMap.get(key2);
        Assert.assertEquals("blabla1",value2.getAttrMap().get("attr1"));
        Assert.assertEquals("blabla2",value2.getAttrMap().get("attr2"));


    }

}