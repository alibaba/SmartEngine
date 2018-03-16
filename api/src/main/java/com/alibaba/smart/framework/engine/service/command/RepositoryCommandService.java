package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;

import java.io.InputStream;

/**
 *
 * 主要负责解析 XML，将其加载到内存里面。
 *
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface RepositoryCommandService {

    ProcessDefinition deploy(String classPathUri) ;

    ProcessDefinition deploy(InputStream inputStream) ;

    ProcessDefinition deployWithUTF8Content(String uTF8ProcessDefinitionContent) ;

    ProcessDefinition checkWithUTF8Content(String processDefinitionId, String processDefinitionVersion, String uTF8ProcessDefinitionContent) ;


}
