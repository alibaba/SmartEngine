package com.alibaba.smart.framework.engine.deployment;

import com.alibaba.smart.framework.engine.deployment.exception.DeployException;

/**
 * Created by ettear on 16-4-12.
 */
public interface Deployer {

    void deploy(String moduleName, String uri) throws DeployException;

    void deploy(String uri) throws DeployException;

}
