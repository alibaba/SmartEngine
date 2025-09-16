package com.alibaba.smart.framework.engine.model.assembly;

import java.util.List;

public interface ProcessDefinitionSource extends NoneIdBasedElement {

    String getTenantId();

    void setTenantId(String tenantId);

    List<ProcessDefinition> getProcessDefinitionList();

    void setProcessDefinitionList(List<ProcessDefinition> processDefinitionList);

    ProcessDefinition getFirstProcessDefinition();
}
