package com.alibaba.smart.framework.engine.extendsion.parser;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.bpmn.assembly.task.ServiceTask;
import com.alibaba.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultIdGenerator;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultProcessEngineConfiguration;
import com.alibaba.smart.framework.engine.configuration.impl.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.constant.ExtensionElementsConstant;
import com.alibaba.smart.framework.engine.extension.scanner.SimpleAnnotationScanner;
import com.alibaba.smart.framework.engine.model.assembly.IdBasedElement;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinitionSource;
import com.alibaba.smart.framework.engine.service.command.ExecutionCommandService;
import com.alibaba.smart.framework.engine.service.command.ProcessCommandService;
import com.alibaba.smart.framework.engine.service.command.RepositoryCommandService;
import com.alibaba.smart.framework.engine.service.query.ExecutionQueryService;
import com.alibaba.smart.framework.engine.smart.PropertyCompositeKey;
import com.alibaba.smart.framework.engine.smart.PropertyCompositeValue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zilong.jiangzl
 * @create 2020-07-17 2:29 下午
 */
public class ExtensionNameSpaceParseTest {


    private SmartEngine smartEngine;

    private RepositoryCommandService repositoryCommandService;

    private ProcessCommandService processCommandService;

    private ExecutionQueryService executionQueryService;

    private ExecutionCommandService executionCommandService;

    @Before
    public void initEngine() {
        //1.初始化
        ProcessEngineConfiguration processEngineConfiguration = new DefaultProcessEngineConfiguration();
        processEngineConfiguration.setIdGenerator(new DefaultIdGenerator());

        processEngineConfiguration.setAnnotationScanner(
                new SimpleAnnotationScanner(
                        SmartEngine.class.getPackage().getName(),
                        ExtensionNameSpaceParseTest.class.getPackage().getName()));
        smartEngine = new DefaultSmartEngine();
        smartEngine.init(processEngineConfiguration);

        //2.获得常用服务
        repositoryCommandService = smartEngine.getRepositoryCommandService();
        processCommandService = smartEngine.getProcessCommandService();
        executionQueryService = smartEngine.getExecutionQueryService();
        executionCommandService = smartEngine.getExecutionCommandService();
    }

    @Test
    public void testCamundaParse() throws Exception {
        testDifferentNsParse("camunda.bpmn20.xml");
    }

    @Test
    public void testFlowableParse() throws Exception {
        testDifferentNsParse("flowable.bpmn20.xml");
    }

    private void testDifferentNsParse(String processDefineName) throws Exception {
        //1. 部署流程定义
        ProcessDefinitionSource processDefinitionSource = repositoryCommandService
                .deploy(String.format("process-def/extension/%s", processDefineName));

        ProcessDefinition processDefinition = processDefinitionSource.getProcessDefinitionList().get(0);
        Assert.assertNotNull(processDefinition);

        for (Map.Entry<String, IdBasedElement> entry : processDefinition.getIdBasedElementMap().entrySet()) {
            if (entry.getValue() instanceof ServiceTask) {
                ServiceTask serviceTask = (ServiceTask) entry.getValue();
                Map<PropertyCompositeKey, String> extensionMap =
                        (Map<PropertyCompositeKey, String>)serviceTask.getExtensionElements().getDecorationMap().get(ExtensionElementsConstant.PROPERTIES);
                Assert.assertTrue(extensionMap.size() == 1);
                final Map<String, Object> properties = new HashMap<String, Object>();
                for(Map.Entry<PropertyCompositeKey, String> e :  extensionMap.entrySet()) {
                    properties.put(e.getKey().getName(), e.getValue());
                }
                PropertyCompositeValue taskOption = (PropertyCompositeValue)properties.get("taskOption");
                Assert.assertEquals(taskOption.getAttrMap().get("value"), "100");
            }
        }
    }
}
