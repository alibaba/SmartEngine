package com.alibaba.smart.framework.engine.deployment;

import com.alibaba.smart.framework.engine.deployment.exception.DeployException;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;
import com.alibaba.smart.framework.engine.runtime.RuntimeProcessComponent;

/**
 * Created by ettear on 16-4-12.
 */
public interface Deployer {
    RuntimeProcessComponent deploy(String moduleName,String uri) throws DeployException;
    RuntimeProcessComponent getProcess(String processId,String version);
}
