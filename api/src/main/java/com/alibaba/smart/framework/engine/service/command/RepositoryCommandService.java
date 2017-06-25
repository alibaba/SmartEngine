package com.alibaba.smart.framework.engine.service.command;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinitionDeployment;

import java.io.InputStream;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface RepositoryCommandService {

    ProcessDefinition deploy(String classPathUri) ;

    ProcessDefinition deploy(InputStream inputStream) ;

    ProcessDefinitionDeployment createDeployment(InputStream inputStream) ;

    void inactivateDeployment(Long id);

    void activateDeployment(Long id);


    void inactivateProcessDefinitionById(String processDefinitionId,String version);

    void activateProcessDefinitionById(String processDefinitionId,String version);

    //void deleteDeployment(Long deploymentId);


}
