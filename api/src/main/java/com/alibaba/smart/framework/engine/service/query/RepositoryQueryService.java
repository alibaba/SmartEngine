package com.alibaba.smart.framework.engine.service.query;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;
import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinitionDeployment;

/**
 * Created by 高海军 帝奇 74394 on 2017 June  14:38.
 */
public interface RepositoryQueryService {

    ProcessDefinition getProcessDefinition(String processDefinitionId,String version);

    ProcessDefinitionDeployment findOne(Long deploymentId);

    ProcessDefinitionDeployment findOne(String processDefinitionId,String version);

    ProcessDefinitionDeployment findAllActiveDeployments();


}
