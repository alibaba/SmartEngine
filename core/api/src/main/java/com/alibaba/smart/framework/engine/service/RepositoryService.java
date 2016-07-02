package com.alibaba.smart.framework.engine.service;

import com.alibaba.smart.framework.engine.exception.DeployException;

 
public interface RepositoryService {

    void deploy(String moduleName, String uri) throws DeployException;

    void deploy(String uri) throws DeployException;

}
