package com.alibaba.smart.framework.engine.service;

import com.alibaba.smart.framework.engine.exception.DeployException;

/**
 * Created by ettear on 16-4-12.
 */
public interface RepositoryService {

    void deploy(String moduleName, String uri) throws DeployException;

    void deploy(String uri) throws DeployException;

}
