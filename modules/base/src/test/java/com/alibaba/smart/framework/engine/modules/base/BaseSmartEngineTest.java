package com.alibaba.smart.framework.engine.modules.base;

import com.alibaba.smart.framework.engine.DefaultSmartEngine;
import com.alibaba.smart.framework.engine.SmartEngine;
import com.alibaba.smart.framework.engine.deployment.Deployer;
import com.alibaba.smart.framework.engine.instance.Instance;
import com.alibaba.smart.framework.engine.instance.InstanceManager;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcessComponent;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ettear on 16-4-14.
 */
public class BaseSmartEngineTest {

    @Test
    public void test() throws Exception{
        SmartEngine smartEngine=new DefaultSmartEngine();
        smartEngine.start();

        Deployer deployer=smartEngine.getDeployer();
        RuntimeProcessComponent component=deployer.deploy(null,"test-process.xml");
        Assert.assertNotNull(component);

        InstanceManager instanceManager=smartEngine.getInstanceManager();
        Instance instance = instanceManager.start("testSmartProcess", "1.0.0", null);


    }
}
