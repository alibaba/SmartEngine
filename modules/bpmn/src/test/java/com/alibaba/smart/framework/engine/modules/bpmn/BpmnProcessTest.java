package com.alibaba.smart.framework.engine.modules.bpmn;

import com.alibaba.smart.framework.engine.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.deployment.ProcessContainer;
import com.alibaba.smart.framework.engine.extensibility.ExtensionPointRegistry;
import com.alibaba.smart.framework.engine.instance.ProcessInstance;
import com.alibaba.smart.framework.engine.instance.manager.ProcessManager;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import org.junit.Assert;
import org.junit.Test;

/**
 * BPMN Test
 * Created by ettear on 16-4-29.
 */
public class BpmnProcessTest {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void test() throws Exception {
        DefaultSmartEngine smartEngine = new DefaultSmartEngine();
        smartEngine.start();

        ExtensionPointRegistry extensionPointRegistry = smartEngine.getExtensionPointRegistry();
        ProcessContainer processContainer = extensionPointRegistry.getExtensionPoint(ProcessContainer.class);

        Deployer deployer = smartEngine.getDeployer();
        deployer.deploy("test-exclusive.bpmn20.xml");

        RuntimeProcess process = processContainer.get("test-exclusive-my", "1.0.0");
        Assert.assertNotNull(process);

        ProcessManager processManager = smartEngine.getProcessManager();
        ProcessInstance instance = processManager.start("test-exclusive-my", "1.0.0", null);
    }
}
