package com.alibaba.smart.framework.engine.deployment;

import com.alibaba.smart.framework.engine.runtime.RuntimeProcess;

/**
 * Created by ettear on 16-4-12.
 */
public interface Deployer {
    RuntimeProcess deploy(String uri);

}
