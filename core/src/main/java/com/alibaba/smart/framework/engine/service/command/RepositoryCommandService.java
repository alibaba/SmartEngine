package com.alibaba.smart.framework.engine.service.command;

import java.io.InputStream;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinitionSource;

/**
 *
 * 主要负责解析 XML，将其加载到内存里面。
 *
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface RepositoryCommandService {

    ProcessDefinitionSource deploy(String classPathResource) ;

    ProcessDefinitionSource deploy(InputStream inputStream) ;

    ProcessDefinitionSource deployWithUTF8Content(String uTF8ProcessDefinitionContent) ;


}
