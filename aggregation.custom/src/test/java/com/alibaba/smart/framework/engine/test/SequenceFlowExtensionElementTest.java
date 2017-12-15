package com.alibaba.smart.framework.engine.test;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.model.assembly.BaseElement;
import com.alibaba.smart.framework.engine.model.assembly.Extensions;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.assembly.impl.AbstractElement;
import com.alibaba.smart.framework.engine.modules.bpmn.assembly.process.SequenceFlow;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.RepositoryQueryService;

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

        RepositoryCommandService repositoryService = smartEngine.getRepositoryCommandService();
        RepositoryQueryService repositoryQueryService = smartEngine.getRepositoryQueryService();


        ProcessDefinition processDefinition = repositoryService
            .deploy("new_sale.bpmn20.xml");
        Assert.assertEquals(38,processDefinition.getProcess().getElements().size());

        processDefinition = repositoryQueryService.getCachedProcessDefinition(processDefinition.getId(),processDefinition.getVersion());

        boolean found  = false;

        List<BaseElement> baseElements =  processDefinition.getProcess().getElements();
        for (BaseElement baseElement : baseElements) {
            if(baseElement instanceof AbstractElement){
                AbstractElement element = (AbstractElement) baseElement;
                if("SequenceFlow_16ifyt3".equals(element.getId())){
                    found = true;
                    Assert.assertEquals("提交机构准入",element.getName());
                }

            }
        }

        baseElements =  processDefinition.getProcess().getElements();
        for (BaseElement baseElement : baseElements) {
            if(baseElement instanceof AbstractElement){
                AbstractElement element = (AbstractElement) baseElement;
                if("InstitutionAdmit".equals(element.getId())){
                    found = true;
                    Assert.assertEquals("机构准入中",element.getName());
                }

            }
        }

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