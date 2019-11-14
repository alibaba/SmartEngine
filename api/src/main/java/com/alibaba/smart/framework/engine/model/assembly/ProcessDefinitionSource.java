package com.alibaba.smart.framework.engine.model.assembly;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;


import lombok.Data;

public interface ProcessDefinitionSource extends BaseElement {

    List<ProcessDefinition> getProcessDefinitionList();

    void setProcessDefinitionList(List<ProcessDefinition> processDefinitionList);

    ProcessDefinition getFirstProcessDefinition();

}
