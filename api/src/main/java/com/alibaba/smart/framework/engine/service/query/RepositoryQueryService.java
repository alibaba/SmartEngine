package com.alibaba.smart.framework.engine.service.query;

import com.alibaba.smart.framework.engine.model.assembly.ProcessDefinition;

/**
 * 获取内存中的流程定义。
 *
 * Created by 高海军 帝奇 74394 on 2017 June  14:38.
 *
 */

public interface RepositoryQueryService {

    ProcessDefinition getCachedProcessDefinition(String processDefinitionId, String version);

}
