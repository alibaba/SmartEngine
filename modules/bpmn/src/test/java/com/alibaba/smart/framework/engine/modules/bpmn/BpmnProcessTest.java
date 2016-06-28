package com.alibaba.smart.framework.engine.modules.bpmn;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.smart.framework.engine.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.manager.ProcessManager;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;

/**
 * BPMN Test Created by ettear on 16-4-29.
 */
public class BpmnProcessTest {

    @Test
    public void testExclusive() throws Exception {
        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.start();

        ExtensionPointRegistry extensionPointRegistry = smartEngine.getExtensionPointRegistry();
        ProcessContainer processContainer = extensionPointRegistry.getExtensionPoint(ProcessContainer.class);

        Deployer deployer = smartEngine.getDeployer();
        deployer.deploy("test-exclusive.bpmn20.xml");

        RuntimeProcess process = processContainer.get("test-exclusive", "1.0.0");
        Assert.assertNotNull(process);

        ProcessManager processManager = smartEngine.getProcessManager();
        Map<String, Object> variables = new HashMap<>();
        variables.put("input", 2);
        ProcessInstance instance = processManager.start("test-exclusive", "1.0.0", variables);
    }

    @Test
    public void testParallel() throws Exception {
        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.start();

        ExtensionPointRegistry extensionPointRegistry = smartEngine.getExtensionPointRegistry();
        ProcessContainer processContainer = extensionPointRegistry.getExtensionPoint(ProcessContainer.class);

        Deployer deployer = smartEngine.getDeployer();
        deployer.deploy("test-parallel.bpmn20.xml");

        RuntimeProcess process = processContainer.get("test-parallel", "1.0.0");
        Assert.assertNotNull(process);

        ProcessManager processManager = smartEngine.getProcessManager();
        Map<String, Object> variables = new HashMap<>();
        ProcessInstance instance = processManager.start("test-parallel", "1.0.0", variables);
    }
}
