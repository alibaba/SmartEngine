package com.alibaba.smart.framework.engine.modules.smart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Extensions;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ettear
 * Created by ettear on 04/08/2017.
 */
public class SequenceFlowExtensionElementTest {

    public static List<String> trace;

    @Before
    public void before(){
        trace=new ArrayList<String>();
    }

    @Test
    public void testDemo() throws Exception {
        ProcessEngineConfiguration processEngineConfiguration  = new DefaultProcessEngineConfiguration();

        SmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        RepositoryCommandService repositoryService = smartEngine
            .getRepositoryCommandService();
        ProcessDefinition processDefinition = repositoryService
            .deploy("demo/new_sale.bpmn20.xml");
        Assert.assertEquals(38,processDefinition.getProcess().getElements().size());

        boolean found  = false;

        List<BaseElement> baseElements =  processDefinition.getProcess().getElements();
        for (BaseElement baseElement : baseElements) {
            if(baseElement instanceof SequenceFlow){
                SequenceFlow sf = (SequenceFlow)baseElement;
                if("SequenceFlow_16ifyt3".equals(sf.getId())){
                    found = true;
                    Extensions extensions = sf.getExtensions();
                    Assert.assertNotNull(extensions);
                }

            }
        }


        Assert.assertTrue(found);


    }

}
