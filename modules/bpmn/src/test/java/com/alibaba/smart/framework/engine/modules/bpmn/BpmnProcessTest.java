package com.alibaba.smart.framework.engine.modules.bpmn;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.smart.framework.engine.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.model.ProcessInstance;
import com.alibaba.smart.framework.engine.pvm.PvmProcess;
import com.alibaba.smart.framework.engine.service.ProcessService;
import com.alibaba.smart.framework.engine.service.RepositoryService;

/**
 * BPMN Test Created by ettear on 16-4-29.
 */
public class BpmnProcessTest {

    @Test
    public void testExclusive() throws Exception {
        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init();

        ExtensionPointRegistry extensionPointRegistry = smartEngine.getExtensionPointRegistry();
        ProcessContainer processContainer = extensionPointRegistry.getExtensionPoint(ProcessContainer.class);

        RepositoryService deployer = smartEngine.getRepositoryService();
        deployer.deploy("test-exclusive.bpmn20.xml");

        PvmProcess process = processContainer.get("test-exclusive", "1.0.0");
        Assert.assertNotNull(process);

        ProcessService processManager = smartEngine.getProcessManager();
        Map<String, Object> variables = new HashMap<>();
        variables.put("input", 2);
        ProcessInstance processInstance = processManager.start("test-exclusive", "1.0.0", variables);
        Assert.assertNotNull(processInstance);
    }

    @Test
    public void testParallel() throws Exception {
        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.init();

        ExtensionPointRegistry extensionPointRegistry = smartEngine.getExtensionPointRegistry();
        ProcessContainer processContainer = extensionPointRegistry.getExtensionPoint(ProcessContainer.class);

        RepositoryService deployer = smartEngine.getRepositoryService();
        deployer.deploy("test-parallel.bpmn20.xml");

        PvmProcess process = processContainer.get("test-parallel", "1.0.0");
        Assert.assertNotNull(process);

        ProcessService processManager = smartEngine.getProcessManager();
        Map<String, Object> variables = new HashMap<>();
        ProcessInstance processInstance = processManager.start("test-parallel", "1.0.0", variables);
        Assert.assertNotNull(processInstance);

    }
}
