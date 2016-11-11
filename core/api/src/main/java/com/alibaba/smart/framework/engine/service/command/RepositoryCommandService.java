package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.exception.DeployException;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface RepositoryCommandService {

	ProcessDefinition deploy(String moduleName, String uri) throws DeployException;

	ProcessDefinition deploy(String uri) throws DeployException;

}
