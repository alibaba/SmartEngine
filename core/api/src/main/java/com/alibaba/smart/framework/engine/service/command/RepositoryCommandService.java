package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.exception.DeployException;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;

 
public interface RepositoryCommandService {

	ProcessDefinition deploy(String moduleName, String uri) throws DeployException;

	ProcessDefinition deploy(String uri) throws DeployException;

}
