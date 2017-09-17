package com.alibaba.smart.framework.engine.service.query;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;

/**
 * Created by 高海军 帝奇 74394 on 2017 June  14:38.
 * TODO
 * FIXME 看待Database DependencyTree 和写个demo，节点名称解析，还有其他问题
 */

public interface RepositoryQueryService {

    ProcessDefinition getProcessDefinition(String processDefinitionId,String version);

}
