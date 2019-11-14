package com.alibaba.smart.framework.engine.test.cases.extensions;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.ExtensionElements;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractTransition;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.test.cases.CustomBaseTestCase;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class SequenceFlowExtensionElementTest extends CustomBaseTestCase {

    public static List<String> trace;



    @Test
    public void testDemo() throws Exception {
        trace = new ArrayList<String>();


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("new_sale.bpmn20.xml").getFirstProcessDefinition();
        Assert.assertEquals(38, processDefinition.getBaseElementList().size());

        processDefinition = repositoryQueryService.getCachedProcessDefinition(processDefinition.getId(),
            processDefinition.getVersion());

        boolean found = false;

        List<BaseElement> baseElements = processDefinition.getBaseElementList();
        for (BaseElement baseElement : baseElements) {
            if (baseElement instanceof AbstractTransition) {
                AbstractTransition element = (AbstractTransition)baseElement;
                if ("SequenceFlow_16ifyt3".equals(element.getId())) {
                    found = true;
                    Assert.assertEquals("提交机构准入", element.getName());
                }

            }
        }

        baseElements = processDefinition.getBaseElementList();
        for (BaseElement baseElement : baseElements) {
            if (baseElement instanceof AbstractTransition) {
                AbstractTransition element = (AbstractTransition)baseElement;
                if ("InstitutionAdmit".equals(element.getId())) {
                    found = true;
                    Assert.assertEquals("机构准入中", element.getName());
                }

            }
        }

        for (BaseElement baseElement : baseElements) {
            if (baseElement instanceof SequenceFlow) {
                SequenceFlow sf = (SequenceFlow)baseElement;
                if ("SequenceFlow_16ifyt3".equals(sf.getId())) {
                    found = true;
                    ExtensionElements extensionElements = sf.getExtensionElements();
                    Assert.assertNotNull(extensionElements);
                }

            }
        }

        Assert.assertTrue(found);

    }

}
