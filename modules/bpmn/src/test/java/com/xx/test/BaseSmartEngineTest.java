package com.xx.test;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.smart.framework.engine.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;

/**
 * Base Smart Engine Test Created by ettear on 16-4-14.
 */
public class BaseSmartEngineTest {

    @Test
    public void test() throws Exception {
        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.start();

        ExtensionPointRegistry extensionPointRegistry = smartEngine.getExtensionPointRegistry();
        ProcessContainer processContainer = extensionPointRegistry.getExtensionPoint(ProcessContainer.class);

        Deployer deployer = smartEngine.getDeployer();
        deployer.deploy(null, "test-exclusive.bpmn20.xml");
        RuntimeProcess process = processContainer.get("test-exclusive-my", "1.0.0");
        Assert.assertNotNull(process);

        // ProcessManager processManager = smartEngine.getProcessManager();
        //
        // ProcessInstance instance = processManager.start("testSmartProcess", "1.0.0", null);
        //

    }
}
