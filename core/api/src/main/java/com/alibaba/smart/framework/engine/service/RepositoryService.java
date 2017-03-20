package com.alibaba.smart.framework.engine.service;

import com.alibaba.smart.framework.engine.exception.DeployException;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;

import java.io.InputStream;


public interface RepositoryService {

	ProcessDefinition deploy(String moduleName, String uri) throws DeployException;

	ProcessDefinition deploy(String uri) throws DeployException;

    ProcessDefinition deploy(InputStream inputStream) throws DeployException;

}
