package com.alibaba.smart.framework.engine.test.cases;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.bpmn.constant.BpmnNameSpaceConstant;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.constant.SmartBase;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElementContainer;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.smart.PropertyCompositeKey;
import com.alibaba.smart.framework.engine.smart.PropertyCompositeValue;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by 高海军 帝奇 74394 on 2017 November  17:54.
 */
public class CompatiableOldVersionProcessDefinitionTest extends CustomBaseTestCase {

    protected void initProcessConfiguration() {
        Map<String,Object> magicExtension = new HashMap();

        Map<String,String> tuples = new HashMap<String, String>();
        tuples.put(SmartBase.SMART_NS,"");
        tuples.put("http://smart.alibaba-test.com/schema/process","smart");
        tuples.put(BpmnNameSpaceConstant.CAMUNDA_NAME_SPACE,"smart");
        tuples.put(BpmnNameSpaceConstant.FLOWABLE_NAME_SPACE,"flowable");
        tuples.put(BpmnNameSpaceConstant.ACTIVITI_NAME_SPACE,"activiti");

        magicExtension.put("fallBack",tuples);

        processEngineConfiguration.setMagicExtension(magicExtension);
    }

    @Test
    public void test() throws Exception {



        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("compatiable-1.x-test.bpmn.xml").getFirstProcessDefinition();
        assertEquals(5, processDefinition.getBaseElementList().size());

        processDefinition = repositoryQueryService.getCachedProcessDefinition(processDefinition.getId(), processDefinition.getVersion());
        assertEquals(5, processDefinition.getBaseElementList().size());

        IdBasedElement idBasedElement1 = processDefinition.getIdBasedElementMap().get(   "ServiceTask_1meueg2");

        ExtensionElementContainer idBasedElement = (ExtensionElementContainer)idBasedElement1;

        ExtensionElements extensionElements = idBasedElement.getExtensionElements();

        Map map = (Map)extensionElements.getDecorationMap().get(
            ExtensionElementsConstant.PROPERTIES);


        PropertyCompositeKey key = new PropertyCompositeKey(null, "task1InParam1");

        boolean flag = map.containsKey(key);
        Assert.assertTrue(flag);


        PropertyCompositeValue actual = (PropertyCompositeValue) map.get(key);
        Assert.assertEquals("process.inParam1", actual.getAttrMap().get("value"));

    }


}